package com.example.pollycall.data.remote

import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("all/")
    suspend fun getCall(
    ): CallResponse<Call>

    @POST("create/")
    suspend fun uploadCall(
        call: Call
    ): CallResponse<String>
}