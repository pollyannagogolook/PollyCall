package com.pollyanna.pollycall.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.pollyanna.pollycall.data.dataclass.ErrorMessage
import com.pollyanna.pollycall.iap.entitlement.Credential
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager
import com.pollyanna.pollycall.iap.entitlement.OemEntitlementManager.USE_CASE_IAP
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepository
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var subscriptionRepository: SubscriptionRepository
) : ViewModel(){

    private var _purchases = MutableStateFlow<List<Purchase>>(emptyList())

    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())


    private var _showPremiumFeatures = MutableStateFlow(false)
    val showPremiumFeatures = _showPremiumFeatures

    private var _productPrice = MutableStateFlow<String?>("")
    val productPrice:StateFlow<String?> = _productPrice

    private var _errorMsg = MutableStateFlow<ErrorMessage?>(null)
    val errorText = _errorMsg

    init {
        // query purchase when the view model is created
        checkIsIapSubscriptionUser { isIapSubscriptionUser ->
            if (isIapSubscriptionUser) {
                _showPremiumFeatures.value = true
            } else {
                // if user is not a subscription user, show product details
                fetchProductPrice()
            }
        }
    }

    // check if the user is a subscription user, if user did not subscribe, show product details
    private fun checkIsIapSubscriptionUser(isIapSubscriptionUser: (Boolean) -> Unit) {
        Log.i(IAP_TAG, "startBillingConnection")
        subscriptionRepository.startBillingConnection { isSuccess ->
            if (!isSuccess) {
                isIapSubscriptionUser(false)
                _errorMsg.value =
                    ErrorMessage(
                        title = "連線失敗",
                        message = "無法連線至 Google Play，請確認網路狀態與 google play 帳號"
                    )
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
                    _errorMsg.value = ErrorMessage(
                        title = "錯誤",
                        message = "無法取得商品資訊，請稍後再試"
                    )
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

    fun removeErrorMsg(){
        _errorMsg.value = null
    }

    fun terminateBillingConnection(){
        subscriptionRepository.terminateBillingConnection()
    }


    override fun onCleared() {
        super.onCleared()
        Log.i(IAP_TAG, "onCleared: LoginViewModel")
    }

}