package com.pollyanna.pollycall.uploadnumber

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyanna.pollycall.data.dataclass.Call
import com.pollyanna.pollycall.data.dataclass.CallResponse
import com.pollyanna.pollycall.data.PollyCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadNumberViewModel @Inject constructor(
    private val pollyRepository: PollyCallRepository
) : ViewModel() {
    companion object {
        private const val TAG = "UploadNumberViewModel"
    }

    private var _uploadResponseFlow = MutableStateFlow<CallResponse>(CallResponse.Loading)
    val uploadResponseFlow = _uploadResponseFlow.asStateFlow()

    fun uploadNumber(call: Call) {
        viewModelScope.launch {
            val response = pollyRepository.uploadCallData(call)
            _uploadResponseFlow.value = response
            Log.d(TAG, "uploadNumber: $response")
        }
    }
}