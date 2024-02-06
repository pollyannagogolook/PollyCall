package com.pollyanna.pollycall

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.pollyanna.pollycall.call_detect.ForegroundService
import com.pollyanna.pollycall.databinding.ActivityMainBinding
import com.pollyanna.pollycall.utils.Constants.Companion.CHANNEL_ID
import com.pollyanna.pollycall.utils.Constants.Companion.DETECT_CALL_TAG

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var foregroundService: ForegroundService
    private lateinit var serviceConnection: ServiceConnection
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestRoleLauncher: ActivityResultLauncher<Intent>

    private val viewModel by viewModels<MainViewModel>()
    private var isBound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createNotificationChannel()
        registerRoleLauncher()
        checkPermissionAndRequestRole()


        lifecycleScope.launch {
            viewModel.phoneInfoFlow.collect { phoneInfo ->
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

    private fun registerRoleLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    checkPermissionAndRequestRole()
                }
            }

        requestRoleLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    Log.i(DETECT_CALL_TAG, "call role has be granted: $intent")
                }
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

    @SuppressLint("InlinedApi")
    private fun checkPermissionAndRequestRole() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ANSWER_PHONE_CALLS,
                    Manifest.permission.CALL_PHONE
                )
            )
        }else{
            requestCallRolePermission()
        }
    }

    @SuppressLint("NewApi")
    private fun requestCallRolePermission() {
        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        requestRoleLauncher.launch(intent)
    }
}

