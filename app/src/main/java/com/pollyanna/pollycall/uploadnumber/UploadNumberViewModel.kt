package com.pollyanna.pollycall.uploadnumber

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyanna.pollycall.data.Call
import com.pollyanna.pollycall.data.CallResponse
import com.pollyanna.pollycall.data.PollyCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadNumberViewModel @Inject constructor(
    private val pollyRepository: PollyCallRepository
) : ViewModel() {
    companion object {
        private const val TAG = "UploadNumberViewModel"
    }

    private var _uploadResponseFlow = MutableStateFlow<CallResponse<Call?>>(CallResponse.Loading())
    val uploadResponseFlow = _uploadResponseFlow

    fun uploadNumber(call: Call) {
        viewModelScope.launch {
            _uploadResponseFlow.value = pollyRepository.uploadCallData(call)
            Log.i(TAG, "uploadNumber: ${_uploadResponseFlow.value.message}")
        }
    }
}