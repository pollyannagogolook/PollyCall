package com.example.pollycall.data

sealed class CallResponse<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T): CallResponse<T>(data)
    class Error<T>(message: String, data: T? = null): CallResponse<T>(data, message)
    class Loading<T>: CallResponse<T>()
}
