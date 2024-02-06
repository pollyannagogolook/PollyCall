package com.pollyanna.pollycall.data.iap.purchase

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val billingClientManager: BillingClientManager
) : SubscriptionRepository {


    private var productDetails: ProductDetails? = null
    private val _billingConnectionState = MutableStateFlow(false)
    override suspend fun startBillingConnection() {
        billingClientManager.startBillingConnection(_billingConnectionState)
    }

    override suspend fun terminateBillingConnection() {
        billingClientManager.terminateBillingConnection()
    }

    // observer product details
    override suspend fun getSubscriptionDetail(){
        billingClientManager.productWithProductDetails.collect { productDetails ->
            if (productDetails.isNotEmpty()) {
                this.productDetails = productDetails.map { it.value }.first()
            }
        }
    }

    // get all purchases
    override fun getPurchases(): Flow<List<Purchase>> = billingClientManager.purchases


    // call billing client to purchase subscription
    override suspend fun purchaseSubscription(activity: Activity) {
        productDetails?.let { billingClientManager.purchaseSubscription(activity, it) }
    }
}