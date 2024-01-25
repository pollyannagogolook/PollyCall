package com.example.pollycall.callDetect

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CallDetectService: Service() {

    private fun startDetectCall(){
        // start the service when app create
    }

    private fun getCallData(){
        // get the call data from repository
    }

    private fun blockScamCall(){

    }
    override fun onBind(p0: Intent?): IBinder? {
       // TODO: Return the block message to activity
        return null
    }
}