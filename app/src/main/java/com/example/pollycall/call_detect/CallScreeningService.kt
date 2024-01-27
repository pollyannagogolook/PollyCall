package com.example.pollycall.call_detect

import android.telecom.Call
import android.telecom.CallScreeningService
import com.example.pollycall.data.PollyCallRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pollyanna Wu on 2024/1/26
 *
 * This class is used to detect incoming call
 * Currently, all calls are allowed
 */
@AndroidEntryPoint
class CallScreeningService @Inject constructor(
    private val repository: PollyCallRepository
) : CallScreeningService() {

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    override fun onScreenCall(callDetails: Call.Details) {

        // get inComing Number
        val inComingNumber = callDetails.handle.schemeSpecificPart

        serviceScope.launch {
            // search call data in data layer
            repository.searchScreenCall(inComingNumber)
        }
    }
}