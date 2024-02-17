package com.pollyanna.pollycall.iap

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.pollyanna.pollycall.iap.entitlement.Credential
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager.USE_CASE_IAP
import com.pollyanna.pollycall.iap.purchase.BillingManager
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepository
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private var subscriptionRepository: SubscriptionRepository
) : AndroidViewModel(application) {

    private var _purchases = MutableStateFlow<List<Purchase>>(emptyList())

    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())


    private var _showPremiumFeatures = MutableStateFlow(false)
    val showPremiumFeatures = _showPremiumFeatures

    private var _productPrice = MutableStateFlow<String?>("")
    val productPrice:StateFlow<String?> = _productPrice

    private var _errorText = MutableStateFlow<String>("")
    val errorText = _errorText

    init {
        // query purchase when the view model is created
        checkIsIapSubscriptionUser { isIapSubscriptionUser ->
            if (isIapSubscriptionUser) {
//                OemEntitlementManager.enablePremiumFeatures(USE_CASE_IAP)
                _showPremiumFeatures.value = true
            } else {
                // if user is not a subscription user, show product details
                fetchProductPrice()
            }

        }
    }

    // check if the user is a subscription user, if user did not subscribe, show product details
    private fun checkIsIapSubscriptionUser(isIapSubscriptionUser: (Boolean) -> Unit) {

        subscriptionRepository.startBillingConnection { isSuccess ->
            if (!isSuccess) {
                isIapSubscriptionUser(false)
                _errorText.value = "Error connecting to Google Play Billing, please try again later."
                return@startBillingConnection
            }
            observePurchases()

            if (_purchases.value.isNotEmpty()) {
                isIapSubscriptionUser(true)
            } else {
                isIapSubscriptionUser(false)
            }
        }
    }


    // fetch premium price
    private fun fetchProductPrice() {
        viewModelScope.launch {
            subscriptionRepository.productDetails.collect { productDetails ->
                if (productDetails.isNotEmpty() || productDetails.firstOrNull() != null) {
                    _productDetails.value = productDetails
                    _productPrice.value =
                        productDetails.first()
                            .subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice

                    Log.i(IAP_TAG, "Product price: ${_productPrice.value}")
                }else{
                    _errorText.value = "Error retrieving product details, please try again later."
                }
            }
        }
    }

    private fun observePurchases() {
        viewModelScope.launch {
            subscriptionRepository.purchases.collect {
                _purchases.value = it
                if (it.isNotEmpty()) {
                    // show success alert
                    OemEntitlementManager.saveCredential(
                        Credential.Iap(it.first().purchaseToken),
                        USE_CASE_IAP
                    )
//                    OemEntitlementManager.enablePremiumFeatures(USE_CASE_IAP)
                    _showPremiumFeatures.value = true
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