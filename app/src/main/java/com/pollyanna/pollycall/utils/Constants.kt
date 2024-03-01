package com.pollyanna.pollycall.utils

class Constants {
    companion object{
        const val CHANNEL_ID = "polly_call_channel"
        const val DETECT_CALL_REQUEST_CODE = 1

        const val ERROR_UNKNOWN = "Unknown error"
        const val ERROR_NOT_FOUND = "查無此電話資訊"

        // TODO tag 建議回歸到各 class 底下
        const val DETECT_CALL_TAG = "DetectCall"
        const val IAP_TAG = "Subscription PollyCall"

        const val PRODUCT_ID = "polly_call_premium"

        const val PURCHASE_SUCCESS = 0
        const val PURCHASE_FAILED = 1
        const val PURCHASE_PENDING = 2
        const val PURCHASE_CANCEL = 3
    }
}