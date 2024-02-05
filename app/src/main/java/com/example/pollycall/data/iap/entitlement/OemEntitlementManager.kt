package com.example.pollycall.data.iap.entitlement

import javax.inject.Inject

/**
 * Author: Pollyanna Wu
 * The [OemEntitlementManager] is a singleton object used to record and manage the login status of the OEM.
 * **/
class OemEntitlementManager @Inject constructor(
    private var entitlementUseCases: Map<String, IEntitlementUseCase> = emptyMap()
) {




}