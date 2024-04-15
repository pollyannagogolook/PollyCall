package com.pollyanna.pollycall

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.pollyanna.pollycall.databinding.ActivityMainBinding
import com.pollyanna.pollycall.utils.Constants.Companion.CHANNEL_ID
import com.pollyanna.pollycall.utils.Constants.Companion.DETECT_CALL_TAG

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestRoleLauncher: ActivityResultLauncher<Intent>

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createNotificationChannel()
        registerRoleLauncher()
        checkPermissionAndRequestRole()

        lifecycleScope.launch {
            viewModel.phoneInfoFlow.collect { phoneInfo ->
                sendNotification(phoneInfo)
            }
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

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(phoneInfo: String){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Polly Call")
            .setContentText(phoneInfo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkPermissionAndRequestRole() {
        requestCallRolePermission()
    }

    @SuppressLint("NewApi")
    private fun requestCallRolePermission() {
        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        startActivityForResult(intent, REQUEST_ID);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(DETECT_CALL_TAG, "call role has be granted")
            }
        }
    }

    companion object{
        private const val NOTIFICATION_ID = 1
        private const val REQUEST_ID = 2
    }
}

