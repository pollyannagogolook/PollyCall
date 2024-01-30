package com.example.pollycall.data.iap

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow


/**
 * Author: Pollyanna Wu
 * The [SubscriptionRepository] is used to abstract the Google Play Billing Library and convert the StateFlow to Flow.
 * */
interface SubscriptionRepository {
    fun checkHasRenewableBasic(): Flow<Boolean>
    fun checkHasPrepaidBasic(): Flow<Boolean>
    fun checkHasRenewablePremium(): Flow<Boolean>
    fun checkHasPrepaidPremium(): Flow<Boolean>
    fun getBasicProductDetails(): Flow<ProductDetails?>
    fun getPremiumProductDetails(): Flow<ProductDetails?>
    fun getPurchases(): Flow<List<Purchase>>

}