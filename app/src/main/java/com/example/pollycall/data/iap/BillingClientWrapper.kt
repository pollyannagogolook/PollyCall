package com.example.pollycall.data.iap

import android.app.Activity
import android.app.Application
import android.content.Context
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
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.example.pollycall.di.PollyCallApplication
import com.example.pollycall.utils.Constants.Companion.IAP_TAG
import com.example.pollycall.utils.Constants.Companion.LIST_OF_PRODUCTS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Author: Pollyanna Wu
 * This is a wrapper to isolate the Google Play Billing's [BillingClient] methods needed
 * **/
class BillingClientWrapper @Inject constructor(context: Application) : PurchasesUpdatedListener,
    ProductDetailsResponseListener {

    // New Subscription ProductDetails
    private val _productWithProductDetails =
        MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
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
    fun startBillingConnection(billingConnectionState: MutableStateFlow<Boolean>) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // if the connection is lost, try to restart the connection
                Log.e(IAP_TAG, "Billing Client Disconnected")
                startBillingConnection(billingConnectionState)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i(IAP_TAG, "Billing response OK")

                    // The BillingClient is ready. You can query purchases here.
                    billingConnectionState.value = true
                    queryPurchases()
                    queryProductDetails()

                } else {
                    Log.e(IAP_TAG, billingResult.debugMessage)
                }
            }
        })
    }

    // launch purchase flow
    fun launchBillingFlow(activity: Activity, params: BillingFlowParams): Int {
        val responseCode = billingClient.launchBillingFlow(activity, params)
        return responseCode.responseCode
    }

    // Query Google Play Billing for existing purchases.
    // New purchase will be notified through onPurchasesUpdated
    fun queryPurchases() {
        if (!billingClient.isReady) {
            Log.e(IAP_TAG, "Billing Client not ready")
        }
        // Query for existing subscription products that have been purchased.
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    _purchases.value = purchaseList
                } else {
                    _purchases.value = emptyList()
                }
            } else {
                Log.e(IAP_TAG, "Error querying purchases: ${billingResult.debugMessage}")
            }
        }
    }

    // Query Google Play Billing for products available to sell and present them to the user.
    fun queryProductDetails() {
        val params = QueryProductDetailsParams.newBuilder()
        val productList = mutableListOf<QueryProductDetailsParams.Product>()

        for (product in LIST_OF_PRODUCTS) {
            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(product)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )
        }
        params.setProductList(productList).let { productDetailsParams ->
            billingClient.queryProductDetailsAsync(productDetailsParams.build(), this)
        }
    }


    // when new purchases returned from the API, PurchaseUpdatedListener.onPurchasesUpdated() will be notified.
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {

            // if the purchase list is not empty, then acknowledge the purchase
            _purchases.value = purchases
            for(purchase in purchases){
                acknowledgePurchase(purchase)
            }

        }
    }

    private fun acknowledgePurchase(purchase: Purchase?) {
        purchase?.let { purchase ->
            if(!purchase.isAcknowledged){
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(params){ billingResult ->
                    if(billingResult.responseCode == BillingClient.BillingResponseCode.OK
                        && purchase.purchaseState == Purchase.PurchaseState.PURCHASED){
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
                var newMap = emptyMap<String, ProductDetails>()
                if (productDetailsList.isNotEmpty()) {
                    newMap = productDetailsList.associateBy { it.productId }
                }
                _productWithProductDetails.value = newMap
            }

            else -> {
                Log.e(IAP_TAG, "Error retrieving product details: $debugMessage")
            }
        }
    }

    // End Billing Client connection
    fun terminateBillingConnection(){
        Log.i(IAP_TAG, "End Billing Client connection")
        billingClient.endConnection()
    }

}