package com.pollyanna.pollycall.iap.prefs

import android.content.Context

class SharedPrefsModule(val context: Context, val name: String, val mode: Int) {
    fun provideContext() = context
    fun provideSharedPreference() = context.getSharedPreferences(name, mode)
}