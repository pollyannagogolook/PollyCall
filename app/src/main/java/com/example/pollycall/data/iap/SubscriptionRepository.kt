package com.example.pollycall.data.iap

import com.android.billingclient.api.ProductDetails
import kotlinx.coroutines.flow.Flow


/**
 * The [SubscriptionDataSource] class is responsible for transforming the data
 * from the [BillingClientWrapper] to [SubscriptionRepositoryImpl]
 * */
interface SubscriptionRepository {
    fun checkUserHasPurchasedApp(): Flow<Boolean>
    fun checkHasPrepaidBasic(): Flow<Boolean>
    fun checkHasRenewablePremium(): Flow<Boolean>
    fun checkHasPrepaidPremium(): Flow<Boolean>
    fun getBasicProductDetails(): Flow<ProductDetails?>
    fun getPremiumProductDetails(): Flow<ProductDetails?>

}