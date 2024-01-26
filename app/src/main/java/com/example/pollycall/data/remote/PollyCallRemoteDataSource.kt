package com.example.pollycall.data.remote

import android.util.Log
import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import javax.inject.Inject

class PollyCallRemoteDataSource @Inject constructor(private val service: ApiService, ) {

    companion object{
        private const val TAG = "PollyCallRemoteDataSource"
    }


    suspend fun getCallData():CallResponse<Call>{
        // get the call data from pollCall server
        return service.getCall()
    }

    suspend fun uploadCallData(call: Call): CallResponse<String>{
        // upload the call data to pollyCall server
        return service.uploadCall(call)
    }
}