package com.example.pollycall

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.pollycall.call_detect.ForegroundService
import com.example.pollycall.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var foregroundService: ForegroundService
    private lateinit var serviceConnection: ServiceConnection
    private val viewModel by viewModels<MainViewModel>()
    private var isBound = false

    companion object {
        const val CHANNEL_ID = "polly_call_channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createNotificationChannel()

        lifecycleScope.launch {
            viewModel.inComingNumberFlow.collect { phoneNumber ->
                viewModel.getPhoneInfo(phoneNumber)
            }
        }

        lifecycleScope.launch {
            viewModel.phoneInfoFlow.collect{phoneInfo ->
               connectToService(phoneInfo)
            }
        }

    }

    override fun onStart() {
        super.onStart()

        Intent(this, ForegroundService::class.java).also { intent ->
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Polly Call Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is Polly Call"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun connectToService(data: String) {
        serviceConnection = object : ServiceConnection {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as ForegroundService.LocalBinder

                foregroundService = binder.getService()
                isBound = true

                // show notifications in the foreground
                foregroundService.showNotification(data)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isBound = false
            }
        }


    }
}

