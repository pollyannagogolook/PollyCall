package com.pollyanna.pollycall.iap.purchase

import android.app.Activity
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * Author: Pollyanna Wu
 * The [SubscriptionRepository] is used to abstract the Google Play Billing Library and convert the StateFlow to Flow.
 * */
interface SubscriptionRepository {
    fun startBillingConnection(isSuccessCallback: (Boolean) -> Unit)
//    suspend fun getSubscriptionDetail()
//    fun getPurchases(): Flow<List<Purchase>>
    suspend fun purchaseSubscription(activity: Activity)
    suspend fun terminateBillingConnection()


}