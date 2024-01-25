package com.example.pollycall.data

import com.example.pollycall.data.local.CallDao
import javax.inject.Inject

class PollyCallRepositoryImpl @Inject constructor(
    private val callDao: CallDao
): PollyCallRepository {
    override fun getCallData(number: String): Call? {
       if (callDao.getCallCache(number) != null) {
           return callDao.getCallCache(number)
       }
        return null

    }


    override fun uploadCallData(call: Call) {
        TODO("Not yet implemented")
    }
}