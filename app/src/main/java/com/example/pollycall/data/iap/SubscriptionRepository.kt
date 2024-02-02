package com.example.pollycall.data.iap

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow


/**
 * Author: Pollyanna Wu
 * The [SubscriptionRepository] is used to abstract the Google Play Billing Library and convert the StateFlow to Flow.
 * */
interface SubscriptionRepository {

    suspend fun getSubscriptionDetail()
    fun getPurchases(): Flow<List<Purchase>>
    suspend fun purchaseSubscription(activity: Activity)
}