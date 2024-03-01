package com.pollyanna.pollycall.call_detect

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.pollyanna.pollycall.data.PollyCallRepository
import com.pollyanna.pollycall.data.dataclass.CallResponse.Success
import com.pollyanna.pollycall.utils.Constants.Companion.DETECT_CALL_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pollyanna Wu on 2024/1/26
 *
 * CallScreeningService performs 2 tasks:
 * 1. Detect incoming call
 * 2. Block incoming call
 *
 * This class is used to detect incoming call
 * Currently, all calls are allowed
 */
@AndroidEntryPoint
class PollyCallScreeningService : CallScreeningService() {

    @Inject
    lateinit var repository: PollyCallRepository

    private val serviceScope = CoroutineScope(Dispatchers.Main)


    companion object {
        const val TAG = "onScreenCall"
    }

    override fun onScreenCall(callDetails: Call.Details) {
        serviceScope.launch {
            // search call data in data layer
            Log.i(DETECT_CALL_TAG, "onScreenCall: $callDetails")

            // get inComing Number
            val incomingNumber = callDetails.handle.schemeSpecificPart

            // TODO 題外話，onScreenCall 在被呼叫的五秒內若沒有執行 respondToCall，會視為 allow call，
            //  所以 check block call 的時間不能超過五秒，這邊一般是不建議進行遠端操作的，但作業階段沒有關係
            val shouldBlock = when (val callResponse = repository.searchScreenCall(incomingNumber)) {
                is Success -> {
                    Log.i(DETECT_CALL_TAG, "onScreenCall: ${callResponse.data}")
                    callResponse.data?.isScam == true
                }
                else -> {
                    Log.i(DETECT_CALL_TAG, "onScreenCall: ${callResponse}")
                    false
                }
            }

            Log.i(DETECT_CALL_TAG, "onScreenCall: shouldBlock=$shouldBlock")
            val response =  CallResponse.Builder()
                .setRejectCall(shouldBlock)
                .setDisallowCall(shouldBlock)
                .setSkipNotification(shouldBlock)
                .setSkipCallLog(shouldBlock)
                .build()
            respondToCall(callDetails, response)
        }
    }
}