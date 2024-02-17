package com.pollyanna.pollycall.iap.purchase

import android.app.Activity
import android.app.Application
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import com.pollyanna.pollycall.utils.Constants.Companion.PRODUCT_ID
import com.pollyanna.pollycall.utils.throwIfDebugBuild
import kotlinx.coroutines.CancellableContinuation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Author: Pollyanna Wu
 * This is a singleton object to isolate the Google Play Billing's [BillingClient] methods needed
 * **/
class BillingManager @Inject constructor(context: Application) : PurchasesUpdatedListener,
    ProductDetailsResponseListener {


    // New Subscription ProductDetails
    private val _productWithProductDetails =
        MutableStateFlow<List<ProductDetails>>(emptyList())
    val productWithProductDetails = _productWithProductDetails.asStateFlow()

    // Current Purchases
    private val _purchases = MutableStateFlow<List<Purchase>>(listOf())
    val purchases = _purchases.asStateFlow()

    // Set to true when a purchase is acknowledged and false when not.
    private val _isNewPurchaseAcknowledged = MutableStateFlow(value = false)
    val isNewPurchaseAcknowledged = _isNewPurchaseAcknowledged.asStateFlow()

    // Initialize the BillingClient.
    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    // launch a connection to Google Play.
    fun startBillingConnection(isSuccessCallback: (Boolean) -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // if the connection is lost, try to restart the connection
                Log.i(IAP_TAG, "Billing Service Disconnected")
                isSuccessCallback(false)
                startBillingConnection(isSuccessCallback)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i(IAP_TAG, "Billing response OK")
                    queryPurchases()
                    queryProductDetails()
                    isSuccessCallback(true)
                } else {
                    isSuccessCallback(false)
                }
            }
        })
    }

    // launch purchase flow
    fun purchaseSubscription(
        activity: Activity,
        productDetails: ProductDetails
    ) {
        val offerToken = productDetails.subscriptionOfferDetails?.get(0)?.offerToken
        offerToken?.let {
            Log.i(IAP_TAG, "Offer token: $offerToken")
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
            try {
                billingClient.launchBillingFlow(activity, billingFlowParams)
            } catch (e: Exception) {
                Log.e(IAP_TAG, "Error launching billing flow: ${e.message}")
                e.throwIfDebugBuild()
            }
        }
    }

    // Provide information about the product to the user
    // New purchase will be notified through onPurchasesUpdated
    fun queryPurchases() {
        if (!billingClient.isReady) {
            Log.e(IAP_TAG, "Billing Client not ready")
            return
        }
        // Query for existing subscription products that have been purchased.
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

        )
        { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.i(IAP_TAG, "response of queryPurchases: ${billingResult.responseCode}")
                if (purchaseList.isNotEmpty()) {
                    _purchases.value = purchaseList
                    Log.i(IAP_TAG, "Purchases: $purchaseList")
                } else {
                    _purchases.value = emptyList()
                }
            } else {
                Log.e(IAP_TAG, "Error querying purchases: ${billingResult.debugMessage}")
            }
        }
    }

    // Provide product information to user.
    fun queryProductDetails() {
        val params = QueryProductDetailsParams.newBuilder()
        val productList = mutableListOf<QueryProductDetailsParams.Product>()

        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_ID)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        params.setProductList(productList).let { productDetailsParams ->
            Log.i(IAP_TAG, "Querying product details: ${productList.first().zza()}")
            billingClient.queryProductDetailsAsync(productDetailsParams.build(), this)
        }


    }


    // Use the PurchasesUpdatedListener to handle new purchases returned from the API
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {

            // if the purchase list is not empty, then acknowledge the purchase
            Log.i(IAP_TAG, "Purchases updated: $purchases")
            _purchases.value = purchases
            for (purchase in purchases) {
                acknowledgePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.e(IAP_TAG, "Error purchasing: ${billingResult.debugMessage}")
        }
    }


    // Perform new subscription purchases' acknowledgement client side.
    private fun acknowledgePurchase(purchase: Purchase?) {
        purchase?.let { purchase ->
            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(params) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                        && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        _isNewPurchaseAcknowledged.value = true
                    }

                }
            }

        }

    }

    override fun onProductDetailsResponse(
        billingResult: BillingResult,
        productDetailsList: MutableList<ProductDetails>
    ) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage

        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                _productWithProductDetails.value = productDetailsList
                Log.i(IAP_TAG, "Product details updated: ${_productWithProductDetails.value}")
            }

            else -> {
                // Handle any other error codes.
                _productWithProductDetails.value = emptyList()
                Log.e(IAP_TAG, "Error retrieving product details: $debugMessage, $responseCode")
                queryProductDetails()
            }
        }
    }

    // End Billing Client connection
    fun terminateBillingConnection() {
        Log.i(IAP_TAG, "End Billing Client connection")
        billingClient.endConnection()
    }


}