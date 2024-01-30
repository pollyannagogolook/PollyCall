package com.example.pollycall.data.iap

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.pollycall.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val billingClientWrapper: BillingClientWrapper) : SubscriptionRepository {

    // check if user has purchased the app
    override fun checkHasRenewableBasic(): Flow<Boolean> =
        billingClientWrapper.purchases.map { purchaseList ->
            purchaseList.any { purchase ->
                purchase.products.contains(Constants.BASIC_SUB) && purchase.isAutoRenewing
            }
        }

    // check if the purchase is prepaid basic subscription
    override fun checkHasPrepaidBasic(): Flow<Boolean> = billingClientWrapper.purchases.map { purchaseList ->
        purchaseList.any { purchase ->
            !purchase.isAutoRenewing && purchase.products.contains(Constants.BASIC_SUB)
        }
    }

    // check if the purchase is auto-renewing premium subscription
    override fun checkHasRenewablePremium(): Flow<Boolean> =
        billingClientWrapper.purchases.map { purchaseList ->
            purchaseList.any { purchase ->
                purchase.products.contains(Constants.BASIC_SUB) && purchase.isAutoRenewing
            }
        }

    // check if the purchase is prepaid premium subscription
    override fun checkHasPrepaidPremium(): Flow<Boolean> =
        billingClientWrapper.purchases.map { purchaseList ->
            purchaseList.any { purchase ->
                !purchase.isAutoRenewing && purchase.products.contains(Constants.BASIC_SUB)
            }
        }

    // get product details for basic subscription
    override fun getBasicProductDetails(): Flow<ProductDetails?> =
        billingClientWrapper.productWithProductDetails.filter { productMap ->
            productMap.containsKey(Constants.BASIC_SUB)
        }.map { basicProductMap ->
            basicProductMap[Constants.BASIC_SUB]
        }

    // get product details for premium subscription
    override fun getPremiumProductDetails(): Flow<ProductDetails?> =
        billingClientWrapper.productWithProductDetails.filter { productMap ->
            productMap.containsKey(Constants.BASIC_SUB)
        }.map { premiumProductMap ->
            premiumProductMap[Constants.BASIC_SUB]
        }

    // get all purchases
    override fun getPurchases(): Flow<List<Purchase>> = billingClientWrapper.purchases
}