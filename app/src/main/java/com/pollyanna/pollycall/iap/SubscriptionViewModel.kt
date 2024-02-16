package com.pollyanna.pollycall.iap

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.pollyanna.pollycall.iap.entitlement.Credential
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager.USE_CASE_IAP
import com.pollyanna.pollycall.iap.purchase.BillingManager
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepository
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepositoryImpl
import com.pollyanna.pollycall.utils.Constants.Companion.PRODUCT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private var subscriptionRepository: SubscriptionRepositoryImpl,
    private var billingClient: BillingManager
) : AndroidViewModel(application) {

    /**
     * The StateFlow of the current purchases
     * This is used to check if the user has any subscription. If [userCurrentSubscriptionFlow] gets updated,
     * should show the user the subscription screen.
     * */

    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())
    val productDetails = _productDetails

    private val _purchases = subscriptionRepository.purchases
    val purchases = _purchases

    private val _enableBuyButton = MutableStateFlow(false)
    val enableBuyButton = _enableBuyButton

    init {
        viewModelScope.launch {
            startBillingConnection()
            observeProductDetails()
        }
    }

    private fun startBillingConnection() {
        subscriptionRepository.startBillingConnection() { isSuccess ->
            if (!isSuccess){
                // show error alert
            }
        }
    }

    private fun observeProductDetails() {
        viewModelScope.launch {
            subscriptionRepository.productDetails.collect {
                if (it.isNotEmpty()) {
                    _productDetails.value = it
                    _enableBuyButton.value = true
                }else{
                    // show error alert
                }
            }
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
            val productDetails = _productDetails.value.firstOrNull()
            if (productDetails != null) {
                subscriptionRepository.purchaseSubscription(activity, productDetails)
            }

        }
    }



    override fun onCleared() {
        super.onCleared()
        billingClient.terminateBillingConnection()
    }

}