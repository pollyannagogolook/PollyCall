package com.example.pollycall.data.iap.entitlement

import android.content.Context
import com.example.pollycall.data.iap.prefs.PrefsIap.PREF_KEY_SUB_UPDATE_TIME
import com.example.pollycall.data.iap.prefs.PrefsRepository
import javax.inject.Inject


/**
 * This class is [IapEntitlementUseCase] is referred to design of Whoscall OEM.
 * This class is used to handle the login situation of the user.
 *
 * */

class IapEntitlementUseCase @Inject constructor(
    delegateUseCase: IEntitlementUseCase,
    private val repository: PrefsRepository
) : IEntitlementUseCase by delegateUseCase {
    override suspend fun refreshEntitlement(callback: IEntitlementStatusCallback?) {
        TODO("Not yet implemented")
    }

    override fun hasLocalLoginHistory(): Boolean {
        return repository.getLong(PREF_KEY_SUB_UPDATE_TIME, 0L) != 0L
    }

    override fun isEntitled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun checkIsEntitlementExpired(): Boolean {
        TODO("Not yet implemented")
    }


    override fun disablePremiumFeatures() {
        TODO("Not yet implemented")
    }


    override fun saveCredential(credential: Credential) {
        TODO("Not yet implemented")
    }
}