package com.example.pollycall.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import java.lang.reflect.Method


@HiltAndroidApp
class PollyCallApplication : Application() {

    val processName: String
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Application.getProcessName()
            } else {
                try {
                    @SuppressLint("PrivateApi")
                    val activityThread = Class.forName("android.app.ActivityThread")

                    @SuppressLint("DiscouragedPrivateApi")
                    val getProcessName: Method =
                        activityThread.getDeclaredMethod("currentProcessName")
                    getProcessName.invoke(null) as String
                } catch (e: Exception) {
                    ""
                }
            }
        }


}