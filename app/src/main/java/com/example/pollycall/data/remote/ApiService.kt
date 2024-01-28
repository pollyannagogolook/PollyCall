package com.example.pollycall.data.remote

import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    suspend fun getCall(
        @Path("{number}")
        number: String
    ): Call

    @POST("create/")
    @FormUrlEncoded
    suspend fun uploadCall(
        @Field("number")number: String,
        @Field("owner")owner: String,
        @Field("is_scam")isScam: Boolean
    ): Call
}