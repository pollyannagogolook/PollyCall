package com.pollyanna.pollycall.call_detect

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.pollyanna.pollycall.data.PollyCallRepository
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

            repository.searchScreenCall(incomingNumber)

            repository.phoneSearchResponse.collect { callResponse ->
                if (callResponse.data?.isScam == true) {
                    Log.i(DETECT_CALL_TAG, "onScreenCall: scam call")
                    // block the call
                    val response =  CallResponse.Builder()
                            .setRejectCall(true)
                            .setDisallowCall(true)
                            .setSkipNotification(true)
                            .setSkipCallLog(true).build()
                    respondToCall(callDetails, response)

                } else {
                    // create a response to the call, currently all calls are allowed
                    val response = CallResponse.Builder()
                            .setRejectCall(false)
                            .setDisallowCall(false)
                            .setSkipNotification(false)
                            .setSkipCallLog(false).build()

                    respondToCall(callDetails, response)
                }
            }

        }
    }
}