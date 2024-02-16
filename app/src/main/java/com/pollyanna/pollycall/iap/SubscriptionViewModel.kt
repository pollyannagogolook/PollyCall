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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private var subscriptionRepository: SubscriptionRepository
) : AndroidViewModel(application) {


    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())

    private val _enableBuyButton = MutableStateFlow(false)
    val enableBuyButton = _enableBuyButton

    init {
        viewModelScope.launch {
            startBillingConnection()
            observeProductDetails()
            observePurchases()
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
    private fun observePurchases() {
        viewModelScope.launch {
            subscriptionRepository.purchases.collect {
                if (it.isNotEmpty()) {
                    // show success alert
                    OemEntitlementManager.saveCredential(
                        Credential.Iap(it.first().purchaseToken),
                        USE_CASE_IAP
                    )
                    OemEntitlementManager.enablePremiumFeatures(USE_CASE_IAP)
                }
            }
        }
    }


    /****
     * Make a purchase through the Google Play Billing Library
     * @param activity [Activity] instance
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
        subscriptionRepository.terminateBillingConnection()
    }

}