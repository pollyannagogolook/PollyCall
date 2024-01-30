package com.example.pollycall.utils

class Constants {
    companion object{
        const val CHANNEL_ID = "polly_call_channel"
        const val DETECT_CALL_REQUEST_CODE = 1
        const val UNKNOWN_ERROR = "Unknown error"
        const val DETECT_CALL_TAG = "DetectCall"
        const val IAP_TAG = "IAP"

        const val BASIC_SUB = "basic_subscription"
        const val PREMIUM_SUB = "premium_subscription"
        val LIST_OF_PRODUCTS = listOf(BASIC_SUB, PREMIUM_SUB)
    }
}