package com.pollyanna.pollycall.data.remote

import android.util.Log
import com.pollyanna.pollycall.data.dataclass.Call
import com.pollyanna.pollycall.data.dataclass.CallResponse
import com.pollyanna.pollycall.utils.Constants
import javax.inject.Inject

class PollyCallRemoteDataSource @Inject constructor(private val service: ApiService) {

    companion object{
        private const val TAG = "PollyCallRemoteDataSource"
    }

    suspend fun getCallData(number: String): CallResponse {
        // get the call data from pollCall server
        return try {
            Log.i(TAG, "getCallData: $number")
            service.getCall(number)?.let {
                CallResponse.Success(it)
            } ?: run {
                CallResponse.Error(Constants.ERROR_NOT_FOUND)
            }
        } catch (e: Exception) {
            Log.i(TAG, "getCallData: $number ${e.message}")
            CallResponse.Error(e.message ?: Constants.ERROR_UNKNOWN)
        }
    }

    suspend fun uploadCallData(call: Call): CallResponse {
        // upload the call data to pollyCall server
        return try {
            CallResponse.Success(service.uploadCall(call.number, call.owner, call.isScam))
        } catch (e: Exception) {
            Log.i(TAG, "uploadCallData: ${e.message}")
            CallResponse.Error(e.message.toString())
        }
    }
}