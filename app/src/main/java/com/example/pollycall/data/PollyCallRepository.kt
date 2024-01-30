package com.example.pollycall.data

import kotlinx.coroutines.flow.Flow

interface PollyCallRepository {
    suspend fun searchScreenCall(inComingNumber: String)
    suspend fun uploadCallData(call: Call): CallResponse<Call?>
    fun shouldBlockCall(number: String): Boolean
    suspend fun getSearchResponse(): Flow<CallResponse<Call?>>


}