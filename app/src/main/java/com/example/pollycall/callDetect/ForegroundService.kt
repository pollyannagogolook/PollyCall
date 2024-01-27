package com.example.pollycall.callDetect

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class NotiService: Service() {

    inner class LocalBinder: Binder(){
        fun getService(): NotiService = this@NotiService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}