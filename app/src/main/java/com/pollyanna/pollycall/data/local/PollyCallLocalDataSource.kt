package com.pollyanna.pollycall.data.local

import com.pollyanna.pollycall.data.dataclass.Call
import com.pollyanna.pollycall.data.remote.ApiService
import javax.inject.Inject

class PollyCallLocalDataSource @Inject constructor(private val dao: CallDao) {

    fun getCallCache(incomingNumber: String) = dao.getCallCache(incomingNumber)

    fun saveCallCache(call: Call) = dao.saveCallCache(call)

    suspend fun saveScamCall(call: Call) = dao.saveScamCallCache(call)
}