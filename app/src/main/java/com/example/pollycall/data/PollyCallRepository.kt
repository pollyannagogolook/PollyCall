package com.example.pollycall.data

interface PollyCallRepository {
    fun getCallData(number: String): Call?
    fun uploadCallData(call: Call)

}