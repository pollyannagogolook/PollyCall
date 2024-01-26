package com.example.pollycall.data.remote

import com.example.pollycall.data.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("all/")
    suspend fun searchCall(
    ): Response<List<Call>>

    @POST("create/")
    suspend fun uploadCall(
        call: Call
    ): Response<Call>
}