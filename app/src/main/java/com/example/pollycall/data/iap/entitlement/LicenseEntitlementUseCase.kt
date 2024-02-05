package com.example.pollycall.data.iap.entitlement

import android.content.Context
import com.example.pollycall.data.iap.license.LicenseEntitlementDataSource
import javax.inject.Inject

/**
 * Author: Pollyanna Wu
 * The [LicenseEntitlementUseCase] is to implement [IEntitlementUseCase] to handle the entitlement of the license key.
 * inject [LicenseEntitlementDataSource] to get the entitlement status.
 * **/

class LicenseEntitlementUseCase @Inject constructor(dataSource: LicenseEntitlementDataSource):
    IEntitlementUseCase {
    override suspend fun refreshEntitlement(callback: IEntitlementStatusCallback?) {
        TODO("Not yet implemented")
    }

    override fun hasLocalLoginHistory(): Boolean {
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

    override fun notifyEntitlementExpired(context: Context) {
        TODO("Not yet implemented")
    }

    override fun saveCredential(credential: Credential) {
        TODO("Not yet implemented")
    }
}