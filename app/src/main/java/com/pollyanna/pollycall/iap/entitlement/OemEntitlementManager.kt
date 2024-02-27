package com.pollyanna.pollycall.iap.entitlement

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Author: Pollyanna Wu
 * The [OemEntitlementManager] is a singleton object used to record and manage the login status of the OEM.
 * **/
object OemEntitlementManager  {
    private const val USE_CASE_LICENSE = "license"
    const val USE_CASE_IAP = "iap"
    private var entitlementUseCase: Map<String, IEntitlementUseCase> = emptyMap()

    init {
        val commonEntitlementUseCase = CommonEntitlementUseCase()
        val licenseEntitlementUseCase = LicenseEntitlementUseCase(commonEntitlementUseCase)
        val iapEntitlementUseCase = IapEntitlementUseCase(commonEntitlementUseCase)

        entitlementUseCase = mapOf(
            USE_CASE_LICENSE to licenseEntitlementUseCase,
            USE_CASE_IAP to iapEntitlementUseCase
        )
    }

    fun setEntitlementUseCase(entitlementUseCase: Map<String, IEntitlementUseCase>) {
        this.entitlementUseCase = entitlementUseCase
    }

    fun refreshEntitlement(callback: IEntitlementStatusCallback? = null) {
        entitlementUseCase.values.filter { it.isEntitled() }
            .asFlow()
            .onEach { it.refreshEntitlement(callback) }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }

    fun enablePremiumFeatures(useCase: String) {
        useCase.let {
            entitlementUseCase[it]?.enablePremiumFeatures()
                ?: throw NoSuchElementException("enablePremiumFeatures: No such EntitlementUseCase: $it")
        }
    }

    fun disablePremiumFeatures(useCase: String) {
        useCase.let {
            entitlementUseCase[it]?.disablePremiumFeatures()
                ?: throw NoSuchElementException("disablePremiumFeatures: No such EntitlementUseCase: $it")
        }
    }

    fun saveCredential(credential: Credential, useCase: String) {
        useCase.let {
            entitlementUseCase[it]?.saveCredential(credential)
                ?: throw NoSuchElementException("saveCredential: No such EntitlementUseCase: $it")
        }
    }

    fun isEntitled(useCase: String): Boolean {
        return useCase.let {
            entitlementUseCase[it]?.isEntitled() ?: false
        }
    }

    fun checkIsEntitlementExpired(useCase: String): Boolean {
        return useCase.let {
            entitlementUseCase[it]?.checkIsEntitlementExpired() ?: false
        }
    }

    fun hasLocalLoginHistory(useCase: String): Boolean {
        return useCase.let {
            entitlementUseCase[it]?.hasLocalLoginHistory() ?: false
        }
    }


}