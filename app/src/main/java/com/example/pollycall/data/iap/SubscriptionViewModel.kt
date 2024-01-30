package com.example.pollycall.data.iap

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails
import com.android.billingclient.api.Purchase
import com.example.pollycall.data.MainState
import com.example.pollycall.di.PollyCallApplication
import com.example.pollycall.utils.Constants.Companion.IAP_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    subscriptionRepository: SubscriptionRepository,
    private var billingClient: BillingClientWrapper
) : AndroidViewModel(application) {
    private val _billingConnectionState = MutableStateFlow(false)


    init {
        billingClient.startBillingConnection(_billingConnectionState)
    }

    // Combine the two products' flows into one
    val productsForSaleFlows = combine(
        subscriptionRepository.getBasicProductDetails(),
        subscriptionRepository.getPremiumProductDetails()
    ) { basicProductDetails, premiumProductDetails ->

        MainState(
            basicProductDetails = basicProductDetails,
            premiumProductDetails = premiumProductDetails
        )
    }

    // Combine the all possible subscriptions' flows into one
    private val userCurrentSubscriptionFlow = combine(
        subscriptionRepository.checkHasPrepaidBasic(),
        subscriptionRepository.checkHasRenewableBasic(),
        subscriptionRepository.checkHasPrepaidPremium(),
        subscriptionRepository.checkHasRenewablePremium()
    ) { hasRenewableBasic, hasPrepaidBasic, hasRenewablePremium, hasPrepaidPremium ->

        MainState(
            hasRenewableBasic = hasRenewableBasic,
            hasPrepaidBasic = hasPrepaidBasic,
            hasRenewablePremium = hasRenewablePremium,
            hasPrepaidPremium = hasPrepaidPremium
        )

    }


    /**
     * The StateFlow of the current purchases
     * This is used to check if the user has any subscription. If [userCurrentSubscriptionFlow] gets updated,
     * should show the user the subscription screen.
     * */
    val currentPurchaseFlow = subscriptionRepository.getPurchases()

    init {
        viewModelScope.launch {
            userCurrentSubscriptionFlow.collectLatest { collectedSubscriptions ->
                when {
                    collectedSubscriptions.hasPrepaidBasic == true &&
                            collectedSubscriptions.hasRenewableBasic == false -> {
                        // User has prepaid basic subscription
                        Log.i(IAP_TAG, "User has prepaid basic subscription")
                    }

                    collectedSubscriptions.hasRenewablePremium == true &&
                            collectedSubscriptions.hasRenewableBasic == false -> {
                        // User has renewable premium subscription
                        Log.i(IAP_TAG, "User has renewable premium subscription")

                    }

                    collectedSubscriptions.hasPrepaidPremium == true &&
                            collectedSubscriptions.hasRenewableBasic == false -> {
                        // User has prepaid premium subscription
                        Log.i(IAP_TAG, "User has prepaid premium subscription")

                    }

                    collectedSubscriptions.hasRenewableBasic == true &&
                            collectedSubscriptions.hasRenewablePremium == false -> {
                        // User has renewable basic subscription
                        Log.i(IAP_TAG, "User has renewable basic subscription")

                    }

                    else -> {
                        // User has no subscription
                        Log.i(IAP_TAG, "User has no subscription")
                    }

                }

            }
        }
    }

    /**
     * Retrieve all eligible plans and offers using tags from ProductDetails
     * @return the eligible offers and base plans in a list.
     * */
    private fun retrieveEligibleOffers(
        offerDetails: MutableList<SubscriptionOfferDetails>,
        tag: String
    ): List<SubscriptionOfferDetails> {
        val eligibleOffers = mutableListOf<SubscriptionOfferDetails>()
        for (offerDetail in offerDetails) {
            if (offerDetail.offerTags.contains(tag)) {
                eligibleOffers.add(offerDetail)
            }
        }
        return eligibleOffers
    }

    /**
     * Calculates the lowest priced offer amongst all eligible offers.
     * @return the lowest priced offer token.
     * */
    private fun leastPricedOfferToken(
        offerDetails: List<SubscriptionOfferDetails>
    ): String {
        var offerToken = String()
        var leastPricedOffer: ProductDetails.SubscriptionOfferDetails
        var lowestPrice = Double.MAX_VALUE

        if (offerDetails.isNotEmpty()) {
            for (offerDetail in offerDetails) {
                for (price in offerDetail.pricingPhases.pricingPhaseList) {
                    if (price.priceAmountMicros < lowestPrice) {
                        lowestPrice = price.priceAmountMicros.toDouble()
                        leastPricedOffer = offerDetail
                        offerToken = leastPricedOffer.offerToken
                    }
                }
            }
        }
        return offerToken
    }


    /***
     * Upgrade or downgrade the subscription plan.
     * @param productDetails the product details of the plan to upgrade or downgrade to.
     * @param oldToken the old subscription token.
     * @return [BillingFlowParams] to launch the billing flow.
     * */
    private fun upDownGradeBillingFlowParamsBuilder(
        productDetails: ProductDetails,
        offerToken: String,
        oldToken: String
    ): BillingFlowParams {
        return BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        ).setSubscriptionUpdateParams(
            BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                .setOldPurchaseToken(oldToken)
                .setReplaceProrationMode(
                    BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_FULL_PRICE
                )
                .build()
        ).build()

    }

    /**
     * Build normal purchase flow params
     * @param productDetails the product details of the plan to upgrade or downgrade to.
     * @param offerToken the offer token of the plan to upgrade or downgrade to.
     * @return [BillingFlowParams] to launch the billing flow.
     * **/

    private fun normalPurchaseBillingFlowParamsBuilder(
        productDetails: ProductDetails,
        offerToken: String
    ): BillingFlowParams {
        return BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        ).build()
    }


    /****
     * Make a purchase through the Google Play Billing Library
     * @param productDetails the product details of the plan to upgrade or downgrade to.
     * @param currentPurchases the current purchases of the user.
     * @param tag the tag of the offer to purchase.
     * @param activity [Activity] instance
     *
     */
    fun buy(
        productDetails: ProductDetails,
        currentPurchases: List<Purchase>?,
        activity: Activity,
        tag: String
    ) {
        val offers = productDetails.subscriptionOfferDetails?.let { productDetails ->
            retrieveEligibleOffers(
                productDetails,
                tag.lowercase()
            )
        }
        val offerToken = offers?.let { leastPricedOfferToken(it) }
        val oldPurchaseToken: String

        // Either upgrade or downgrade, or conversion purchase
        if (!currentPurchases.isNullOrEmpty() &&
            currentPurchases.size == MAX_CURRENT_PURCHASES_ALLOWED) {

            // get current purchase token
            oldPurchaseToken = currentPurchases.first().purchaseToken

            val billingParams = offerToken?.let { token ->
                upDownGradeBillingFlowParamsBuilder(
                    productDetails,
                    token,
                    oldPurchaseToken
                )
            }

            billingParams?.let { params ->
                billingClient.launchBillingFlow(
                    activity, params
                )
            }
        }else if(!currentPurchases.isNullOrEmpty()
            && currentPurchases.size < MAX_CURRENT_PURCHASES_ALLOWED){

            // The developer has allowed users  to have more than 1 purchase
            Log.d(IAP_TAG, "User has more than 1 current purchase.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        billingClient.terminateBillingConnection()
    }

    companion object{
        private const val MAX_CURRENT_PURCHASES_ALLOWED = 1
    }
}