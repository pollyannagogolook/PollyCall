package com.pollyanna.pollycall.data.remote

import com.pollyanna.pollycall.data.dataclass.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/catalog/number/{number}")
    suspend fun getCall(
        @Path("number")
        number: String
    ): Call?

    @POST("/catalog/number/create/")
    @FormUrlEncoded
    suspend fun uploadCall(
        @Field("number")number: String,
        @Field("owner")owner: String,
        @Field("is_scam")isScam: Boolean
    ): Call
}