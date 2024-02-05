package com.example.pollycall.data.iap.prefs

interface IPrefsDefaultValueFactory {
    fun getString(key: String): String?
    fun getBoolean(key: String): Boolean?
    fun getInt(key: String): Int?
    fun getLong(key: String): Long?
}