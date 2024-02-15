package com.pollyanna.pollycall.iap.entitlement

import android.content.Context
import com.pollyanna.pollycall.iap.entitlement.Credential
import com.pollyanna.pollycall.iap.entitlement.IEntitlementStatusCallback
import com.pollyanna.pollycall.iap.entitlement.IEntitlementUseCase
import com.pollyanna.pollycall.iap.license.LicenseEntitlementDataSource
import com.pollyanna.pollycall.iap.license.PrefLicense
import javax.inject.Inject

/**
 * Author: Pollyanna Wu
 * The [LicenseEntitlementUseCase] is to implement [IEntitlementUseCase] to handle the entitlement of the license key.
 * inject [LicenseEntitlementDataSource] to get the entitlement status.
 * **/

class LicenseEntitlementUseCase @Inject constructor(delegateUseCase: IEntitlementUseCase):
    IEntitlementUseCase by delegateUseCase{

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
        (credential as? Credential.License)?.let {
            if (it.licenseKey.isEmpty() || it.uid.isEmpty()) {
                throw Exception("saveCredential: licenseKey or uid is empty")
            }
        } ?: throw Exception("saveCredential: credential is null or not License type")
    }
}