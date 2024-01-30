package com.example.pollycall.data

import kotlinx.coroutines.flow.Flow

interface PollyCallRepository {
    suspend fun searchScreenCall(inComingNumber: String): CallResponse<Call?>
    suspend fun uploadCallData(call: Call): CallResponse<Call?>
    fun shouldBlockCall(number: String): Boolean
    suspend fun getInComingNumber(): Flow<String?>


}