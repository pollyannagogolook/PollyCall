package com.pollyanna.pollycall.iap.entitlement

import com.pollyanna.pollycall.iap.prefs.PrefsIap.PREF_KEY_OEM_PRODUCT_ID
import com.pollyanna.pollycall.iap.prefs.PrefsIap.PREF_KEY_SUB_UPDATE_TIME
import com.pollyanna.pollycall.iap.prefs.PrefsRepository
import com.pollyanna.pollycall.iap.purchase.BillingClientManager
import com.pollyanna.pollycall.utils.Constants.Companion.PRODUCT_ID
import com.pollyanna.pollycall.utils.ProcessManager
import javax.inject.Inject


/**
 * This class is [IapEntitlementUseCase] is referred to design of Whoscall OEM.
 * This class is used to handle the login situation of the user.
 *
 * */

class IapEntitlementUseCase @Inject constructor(
    delegateUseCase: IEntitlementUseCase
) : IEntitlementUseCase by delegateUseCase {


    // when user get purchase, enable premium features
    override suspend fun refreshEntitlement(callback: IEntitlementStatusCallback?) {
//        val productId = PRODUCT_ID
//
//        billingClientManager.purchases.collect{ purchaseList ->
//            if (purchaseList.isNotEmpty()){
//                saveCredential(Credential.Iap(productId))
//                enablePremiumFeatures()
//            } else {
//                val isFirstTimeExpired = checkIsEntitlementExpired()
//                disablePremiumFeatures()
//                if (isFirstTimeExpired) processManager.handleLicenseExpired()
//            }
//            callback?.onResult(true)
//        }
    }
    override fun hasLocalLoginHistory(): Boolean {
//        return repository.getLong(PREF_KEY_SUB_UPDATE_TIME, 0L) != 0L
        return false
    }

    override fun isEntitled(): Boolean {
//        return repository.getString(PREF_KEY_SUB_UPDATE_TIME, "")?.isNotEmpty() ?: false
        return false
    }

    override fun checkIsEntitlementExpired(): Boolean {
        return !isEntitled() && hasLocalLoginHistory()
    }


    override fun disablePremiumFeatures() {
//       repository.remove(PREF_KEY_OEM_PRODUCT_ID)
    }


    override fun saveCredential(credential: Credential) {
//        if (credential is Credential.Iap) {
//            repository.apply {
//                apply(PREF_KEY_OEM_PRODUCT_ID, credential.productId)
//                apply(PREF_KEY_SUB_UPDATE_TIME, System.currentTimeMillis())
//            }
//        } else throw IllegalArgumentException("credential type not support")
    }
}