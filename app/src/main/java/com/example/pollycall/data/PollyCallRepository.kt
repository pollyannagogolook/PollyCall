package com.example.pollycall.data

import kotlinx.coroutines.flow.Flow

interface PollyCallRepository {
    suspend fun getCallData(number: String): Flow<CallResponse<Call?>>
    suspend fun uploadCallData(call: Call): Flow<CallResponse<Call>>

     fun shouldBlockCall(number: String): Boolean

}