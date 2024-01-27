package com.example.pollycall.data.remote

import android.util.Log
import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import javax.inject.Inject

class PollyCallRemoteDataSource @Inject constructor(private val service: ApiService, ) {

    companion object{
        private const val TAG = "PollyCallRemoteDataSource"
    }


    suspend fun getCallData():CallResponse<Call>{
        // get the call data from pollCall server
        return try {
            CallResponse.Success(service.getCall())
        } catch (e: Exception) {
            Log.i(TAG, "getCallData: ${e.message}")
            CallResponse.Error(e.message.toString(), null)
        }

    }

    suspend fun uploadCallData(call: Call): CallResponse<String>{
        // upload the call data to pollyCall server
        return try {
            CallResponse.Success(service.uploadCall(call.number, call.owner, call.isScam))
        } catch (e: Exception) {
            Log.i(TAG, "uploadCallData: ${e.message}")
            CallResponse.Error(e.message.toString(), null)
        }
    }
}