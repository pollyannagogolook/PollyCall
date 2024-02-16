package com.pollyanna.pollycall.iap.purchase

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val billingManager: BillingManager
) : SubscriptionRepository {


    private var _productDetails: ProductDetails? = null

    override fun startBillingConnection(isSuccessCallback: (Boolean) -> Unit){
        billingManager.startBillingConnection(isSuccessCallback)
    }

//    override suspend fun startBillingConnection(connectionState: MutableStateFlow<Boolean>) {
//        billingManager.startBillingConnection(connectionState)
//    }

    override suspend fun terminateBillingConnection() {
        billingManager.terminateBillingConnection()
    }

    // observer product details
    override suspend fun getSubscriptionDetail(){
        billingManager.productWithProductDetails.collectLatest { productDetails ->
            if (productDetails.isNotEmpty()) {
                _productDetails = productDetails.map { it.value }.first()
                Log.i(IAP_TAG, "repository getSubscriptionDetail is updated ${_productDetails!!.productId}")
            }
        }
    }

    // get all purchases
    override fun getPurchases(): Flow<List<Purchase>> = billingManager.purchases


    // call billing client to purchase subscription
    override suspend fun purchaseSubscription(activity: Activity) {
        Log.i(IAP_TAG, "repository purchaseSubscription")
        _productDetails?.let {
        Log.i(IAP_TAG, "repository purchaseSubscription and productDetails is not null")
            billingManager.purchaseSubscription(activity, it) }
    }
}