package com.pollyanna.pollycall.data.local

sealed class Credential {
    data class License(val licenseKey: String, val uid: String) : Credential()
}