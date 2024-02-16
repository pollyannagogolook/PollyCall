package com.pollyanna.pollycall.iap.purchase

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepository @Inject constructor(
    private val billingManager: BillingManager
) {

    // get all purchases
    val purchases: Flow<List<Purchase>> = billingManager.purchases

    val productDetails: Flow<List<ProductDetails>> = billingManager.productWithProductDetails

    fun startBillingConnection(isSuccessCallback: (Boolean) -> Unit) {
        billingManager.startBillingConnection(isSuccessCallback)
    }


    fun terminateBillingConnection() {
        billingManager.terminateBillingConnection()
    }



    // call billing client to purchase subscription
    fun purchaseSubscription(activity: Activity, productDetails: ProductDetails?) {
        Log.i(IAP_TAG, "repository purchaseSubscription")
        productDetails?.let {
            Log.i(IAP_TAG, "repository purchaseSubscription and productDetails is not null")
            billingManager.purchaseSubscription(activity, it)
        }
    }
}