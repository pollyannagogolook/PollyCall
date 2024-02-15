package com.pollyanna.pollycall.iap.entitlement

import android.content.Context

/**
 * This class is to implement [IEntitlementUseCase] to handle the entitlement of the license key.
 * */
class CommonEntitlementUseCase: IEntitlementUseCase {

    override suspend fun refreshEntitlement(callback: IEntitlementStatusCallback?) {
        TODO("Not yet implemented")
    }

    override fun isEntitled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun checkIsEntitlementExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun enablePremiumFeatures() {
        TODO("Not yet implemented")
    }

    override fun disablePremiumFeatures() {
        TODO("Not yet implemented")
    }

    override fun hasLocalLoginHistory(): Boolean {
        TODO("Not yet implemented")
    }

    override fun notifyEntitlementExpired(context: Context) {
        TODO("Not yet implemented")
    }

    override fun saveCredential(credential: Credential) {
        TODO("Not yet implemented")
    }
}