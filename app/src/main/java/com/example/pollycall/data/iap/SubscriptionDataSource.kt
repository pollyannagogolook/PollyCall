package com.example.pollycall.data.iap

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * The [SubscriptionDataSource] class is responsible for transforming the data
 * from the [BillingClientWrapper] to [PollyCallRepositoryImpl]
 * */
//class SubscriptionDataSource @Inject constructor(private val billingClientWrapper: BillingClientWrapper) {
//    // Set to true whether purchase is an auto-renewing basic subscription.
//    fun checkHasRenewableBasic(): Flow<Boolean> {
//        return billingClientWrapper.purchases.map { purchaseList ->
//            purchaseList.any { purchase ->
//                purchase.products.contains(BASIC_SUB) && purchase.isAutoRenewing
//            }
//        }
//    }
//}