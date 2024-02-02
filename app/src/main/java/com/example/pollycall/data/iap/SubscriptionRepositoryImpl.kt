package com.example.pollycall.data.iap

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.pollycall.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val billingClientManager: BillingClientManager
) : SubscriptionRepository {


    private var productDetails: ProductDetails? = null

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