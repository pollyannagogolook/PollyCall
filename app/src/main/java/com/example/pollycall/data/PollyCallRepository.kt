package com.example.pollycall.data

interface PollyCallRepository {
    suspend fun getCallData(number: String): CallResponse<Call?>
    suspend fun uploadCallData(call: Call): CallResponse<Call>

}