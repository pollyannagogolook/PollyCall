package com.pollyanna.pollycall.data.remote

import android.util.Log
import com.pollyanna.pollycall.data.Call
import com.pollyanna.pollycall.data.CallResponse
import javax.inject.Inject

class PollyCallRemoteDataSource @Inject constructor(private val service: ApiService, ) {

    companion object{
        private const val TAG = "PollyCallRemoteDataSource"
    }


    suspend fun getCallData(number: String): CallResponse<Call> {
        // get the call data from pollCall server
        return try {
            Log.i(TAG, "getCallData: $number")
            CallResponse.Success(service.getCall(number))

        } catch (e: Exception) {

            Log.i(TAG, "getCallData: $number ${e.message}")
            CallResponse.Error(e.message.toString(), null)
        }

    }

    suspend fun uploadCallData(call: Call): CallResponse<Call> {
        // upload the call data to pollyCall server
        return try {
            CallResponse.Success(service.uploadCall(call.number, call.owner, call.isScam))
        } catch (e: Exception) {
            Log.i(TAG, "uploadCallData: ${e.message}")
            CallResponse.Error(e.message.toString(), null)
        }
    }
}