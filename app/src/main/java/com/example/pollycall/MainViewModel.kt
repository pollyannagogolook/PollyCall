package com.example.pollycall

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainViewModel: ViewModel(){

    fun shouldBlock(number: String): Boolean{
        return false
    }
}