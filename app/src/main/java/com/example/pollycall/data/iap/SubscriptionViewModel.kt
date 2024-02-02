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
    private var subscriptionRepository: SubscriptionRepository,
    private var billingClient: BillingClientManager
) : AndroidViewModel(application) {
    private val _billingConnectionState = MutableStateFlow(false)


    init {
        billingClient.startBillingConnection(_billingConnectionState)
    }


    /**
     * The StateFlow of the current purchases
     * This is used to check if the user has any subscription. If [userCurrentSubscriptionFlow] gets updated,
     * should show the user the subscription screen.
     * */
    val currentPurchaseFlow = subscriptionRepository.getPurchases()

    init {
        viewModelScope.launch {

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



    /****
     * Make a purchase through the Google Play Billing Library
     * @param productDetails the product details of the plan to upgrade or downgrade to.
     * @param currentPurchases the current purchases of the user.
     * @param tag the tag of the offer to purchase.
     * @param activity [Activity] instance
     *
     */
    fun buy(
        activity: Activity
    ) {
        viewModelScope.launch {
            subscriptionRepository.purchaseSubscription(
                activity
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        billingClient.terminateBillingConnection()
    }

    companion object {
        private const val MAX_CURRENT_PURCHASES_ALLOWED = 1
    }
}