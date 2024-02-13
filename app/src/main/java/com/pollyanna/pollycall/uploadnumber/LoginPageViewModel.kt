package com.pollyanna.pollycall.uploadnumber

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val repository: SubscriptionRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.getSubscriptionDetail()
        }
    }

    fun purchaseSubscription(activity: Activity) {
        viewModelScope.launch {
            repository.purchaseSubscription(activity)
        }
    }
}