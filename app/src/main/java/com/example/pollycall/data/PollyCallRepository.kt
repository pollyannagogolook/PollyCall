package com.example.pollycall.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PollyCallRepository {
    suspend fun searchScreenCall(number: String): Flow<CallResponse<Call?>>
    suspend fun uploadCallData(call: Call): Flow<CallResponse<Call>>
    fun shouldBlockCall(number: String): Boolean
    fun getScreenCall(): StateFlow<String>


}