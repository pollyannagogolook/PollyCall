package com.pollyanna.pollycall.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PollyCallApplication : Application() {

//    val processName: String
//        get() {
//            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                Application.getProcessName()
//            } else {
//                try {
//                    @SuppressLint("PrivateApi")
//                    val activityThread = Class.forName("android.app.ActivityThread")
//
//                    @SuppressLint("DiscouragedPrivateApi")
//                    val getProcessName: Method =
//                        activityThread.getDeclaredMethod("currentProcessName")
//                    getProcessName.invoke(null) as String
//                } catch (e: Exception) {
//                    ""
//                }
//            }
//        }


}