package com.example.pollycall.data.iap

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pollycall.data.iap.purchase.BillingClientManager
import com.example.pollycall.data.iap.purchase.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private var subscriptionRepository: SubscriptionRepository,
    private var billingClient: BillingClientManager
) : AndroidViewModel(application) {

    /**
     * The StateFlow of the current purchases
     * This is used to check if the user has any subscription. If [userCurrentSubscriptionFlow] gets updated,
     * should show the user the subscription screen.
     * */


    init {
        viewModelScope.launch {
            subscriptionRepository.startBillingConnection()
            onPurchaseObserve()
        }
    }




    /****
     * Make a purchase through the Google Play Billing Library
     * @param productDetails the product details of the plan to upgrade or downgrade to.
     * @param currentPurchases the current purchases of the user.
     * @param tag the tag of the offer to purchase.
     * @param activity [Activity] instance
     *
     */
    fun buy(activity: Activity) {
        viewModelScope.launch {
            subscriptionRepository.purchaseSubscription(activity)
        }
    }


    private suspend fun onPurchaseObserve() {
        subscriptionRepository.getPurchases().collect{ newPurchase ->
            // should save credentials
            // should enable premium features

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