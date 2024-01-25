package com.example.pollycall.callDetect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi

class CallEventBroadcastReceiver : BroadcastReceiver() {
    companion object {

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {

        val telephoneManager: TelephonyManager =
            context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager


        telephoneManager.registerTelephonyCallback(
            context.mainExecutor,
            object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int) {
                    when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {

                        }

                        TelephonyManager.CALL_STATE_RINGING -> {
                            val incomingNumber =
                                intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                            intent?.putExtra("incomingNumber", incomingNumber)
                        }


                        TelephonyManager.CALL_STATE_OFFHOOK -> {

                        }
                    }
                }
            }
        )


    }
}