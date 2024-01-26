package com.example.pollycall.callDetect

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission

class CallEventBroadcastReceiver : BroadcastReceiver() {
    companion object {

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent) {

        val telephonyManager =
            context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                    override fun onCallStateChanged(state: Int) {
                        if (checkSelfPermission(
                                context,
                                android.Manifest.permission.READ_PHONE_STATE
                            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                        ) {

                            return
                        }
                        if (state == TelephonyManager.CALL_STATE_RINGING) {
                            val phoneNumber =
                                intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                            val stateOffHook = TelephonyManager.EXTRA_STATE_OFFHOOK
                            val stateIdle = TelephonyManager.EXTRA_STATE_IDLE


                        }
                    }
                })
        }
    }
}
