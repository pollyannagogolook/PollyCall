package com.pollyanna.pollycall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyanna.pollycall.data.CallResponse
import com.pollyanna.pollycall.data.PollyCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PollyCallRepository) : ViewModel() {


    private var _phoneInfoFlow = MutableStateFlow<String>("")
    val phoneInfoFlow: StateFlow<String> = _phoneInfoFlow

    init {
        getPhoneInfo()
    }

    private fun getPhoneInfo() {
        viewModelScope.launch {
            repository.getSearchResponse().collect { callResponse ->
                if (callResponse is CallResponse.Success) {
                    callResponse.data?.let {
                        _phoneInfoFlow.value =
                            "你接到一通來自 ${it.owner} 的電話，電話號碼為 ${it.number}"
                    } ?: let {
                        _phoneInfoFlow.value = "查無此電話資訊"
                    }

                } else if (callResponse is CallResponse.Error) {
                    _phoneInfoFlow.value = "查詢電話時遇到錯誤：${callResponse.message}"
                }

            }
        }
    }
}