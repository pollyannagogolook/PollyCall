package com.example.pollycall.uploadnumber

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import com.example.pollycall.data.PollyCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadNumberViewModel @Inject constructor(private val repository: PollyCallRepository): ViewModel(){
    companion object{
        private const val TAG = "UploadNumberViewModel"
    }

    private var _uploadResponseFlow = MutableStateFlow<CallResponse<Call>>(CallResponse.Loading())
    val uploadResponseFlow = _uploadResponseFlow

        fun uploadNumber(call: Call){
            viewModelScope.launch {
                _uploadResponseFlow.value = repository.uploadCallData(call).first()
                Log.i(TAG, "uploadNumber: ${_uploadResponseFlow.value.message}")
            }
        }
}