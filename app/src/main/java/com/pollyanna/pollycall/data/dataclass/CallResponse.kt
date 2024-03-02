package com.pollyanna.pollycall.data.dataclass

sealed class CallResponse {
    class Success(val data: Call?): CallResponse()
    class Error(val message: String): CallResponse()
    object Loading: CallResponse()
}
