package com.pollyanna.pollycall.data.iap.entitlement

import android.content.Context

/**
 * This interface [IEntitlementUseCase] is referred to design of Whoscall OEM.
 * There are 2 methods to login, one is by license key, the other is by IAP.
 * Therefore, the usecase to check entitlement is designed to be able to handle multiple login cases.
 * */

interface IEntitlementUseCase {
    suspend fun refreshEntitlement(callback: IEntitlementStatusCallback?)
    fun hasLocalLoginHistory(): Boolean
    fun isEntitled(): Boolean
    fun checkIsEntitlementExpired(): Boolean
    fun enablePremiumFeatures()
    fun disablePremiumFeatures()
    fun notifyEntitlementExpired(context: Context)
    fun saveCredential(credential: Credential)
}

sealed class Credential {
    data class License(val licenseKey: String, val uid: String) : Credential()
    data class Iap(val productId: String) : Credential()
}

interface IEntitlementStatusCallback {
    fun onResult(success: Boolean)
}