package com.pollyanna.pollycall.iap.purchase

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.pollyanna.pollycall.di.PollyCallApplication
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepository @Inject constructor(
    private val billingManager: BillingManager,
    private val context: Application
) {

    // get all purchases
    val purchases: Flow<List<Purchase>> = billingManager.purchases

    val productDetails: Flow<List<ProductDetails>> = billingManager.productWithProductDetails

    fun startBillingConnection(isSuccessCallback: (Boolean) -> Unit) {
        checkInternetConnection(context) { hasInternet ->
            if (hasInternet) {
                billingManager.startBillingConnection(isSuccessCallback)
            } else {
                isSuccessCallback(false)
            }
        }
    }

    fun terminateBillingConnection() {
        billingManager.terminateBillingConnection()
    }


    // call billing client to purchase subscription
    fun purchaseSubscription(activity: Activity, productDetails: ProductDetails): BillingResult {

        return billingManager.purchaseSubscription(activity, productDetails)
    }


    // check internet connection
    private fun checkInternetConnection(context: Context, hasInternet: (Boolean) -> Unit) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return hasInternet(false)
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(network) ?: return hasInternet(false)
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> hasInternet(true)

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> hasInternet(true)

            else -> hasInternet(false)
        }
    }
}