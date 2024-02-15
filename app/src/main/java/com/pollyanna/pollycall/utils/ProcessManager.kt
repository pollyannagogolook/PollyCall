package com.pollyanna.pollycall.utils


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pollyanna.pollycall.di.PollyCallApplication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This object [ProcessManager] is referred to design of Whoscall OEM.
 * This object is used to handle the process of the App
 *
 * **/
@Singleton
class ProcessManager @Inject constructor(val application: PollyCallApplication) {

    var mainPid: Int = 0
//    fun isInMainProcess(): Boolean {
//        return if (mainPid != 0) {
//            android.os.Process.myPid() == mainPid
//        } else {
//           application.processName == BuildConfig.APPLICATION_ID
//        }
//    }

    fun clearAllTasks() {
        val context = application.applicationContext
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return

        manager.appTasks.forEach {
            it.finishAndRemoveTask()
        }
    }

    fun isOurAppInForeground(): Boolean {
        val context = application.applicationContext
        val runningList = (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.runningAppProcesses
                ?: return false

        return runningList.any {
            it.uid == context.applicationInfo.uid && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }

    fun restart() {
        Log.i("ProcessManager", "restart")
//        MainActivity.launch(application.applicationContext, android.os.Process.myPid())
    }

    fun handleLicenseExpired() {
        val context = application.applicationContext
        val isAppForeground = isOurAppInForeground()
        if (!isAppForeground) {
            clearAllTasks()
        } else {
            val intent = Intent(context, LoginPage::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
        }
    }
}