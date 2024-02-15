package com.pollyanna.pollycall.iap

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pollyanna.pollycall.iap.entitlement.Credential
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager.USE_CASE_IAP
import com.pollyanna.pollycall.iap.purchase.BillingClientManager
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepository
import com.pollyanna.pollycall.utils.Constants
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import com.pollyanna.pollycall.utils.Constants.Companion.PRODUCT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
            subscriptionRepository.getSubscriptionDetail()
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
        subscriptionRepository.getPurchases().collect { newPurchase ->
            if (newPurchase.isEmpty()) {
                return@collect
            }
            OemEntitlementManager.saveCredential(Credential.Iap(PRODUCT_ID), USE_CASE_IAP)
            OemEntitlementManager.enablePremiumFeatures(USE_CASE_IAP)
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