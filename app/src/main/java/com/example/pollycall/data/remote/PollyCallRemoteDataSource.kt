package com.example.pollycall.data.remote

import android.util.Log
import com.example.pollycall.data.Call
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import javax.inject.Inject

class PollyCallRemoteDataSource @Inject constructor(private val service: ApiService, ) {

    companion object{
        private const val TAG = "PollyCallRemoteDataSource"
    }


    suspend fun getCallData(): Response<List<Call>>{
        // get the call data from pollCall server
        return service.searchCall()
    }

    suspend fun uploadCallData(call: Call){
        // upload the call data to pollyCall server
        service.uploadCall(call)
    }
}