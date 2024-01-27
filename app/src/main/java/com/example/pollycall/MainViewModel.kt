package com.example.pollycall

import androidx.lifecycle.ViewModel
import com.example.pollycall.data.PollyCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(repository: PollyCallRepository): ViewModel(){


}