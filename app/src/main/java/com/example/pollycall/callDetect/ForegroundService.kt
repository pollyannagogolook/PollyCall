package com.example.pollycall.callDetect

import android.R
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.pollycall.MainActivity.Companion.CHANNEL_ID


class ForegroundService: Service(){
    companion object{
        const val CHANNEL_NAME = "Polly Call Channel"
        const val CHANNEL_DESCRIPTION = "This is Polly Call"
        const val NOTIFICATION_TITLE = "Polly Call"
        const val NOTIFICATION_ID = 1
    }

    inner class LocalBinder: Binder(){
        fun getService(): ForegroundService = this@ForegroundService
    }

    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()

    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(data: String): Notification {

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(data)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .build()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(data: String){
        val notification = createNotification(data)

        startForeground(NOTIFICATION_ID, notification)
    }


}