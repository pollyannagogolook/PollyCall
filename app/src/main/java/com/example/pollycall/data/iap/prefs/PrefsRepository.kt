package com.example.pollycall.data.iap.prefs

import android.app.Application
import android.content.Context
import android.content.SharedPreferences.Editor
import javax.inject.Inject

/**
 * This class is [PrefsRepository] is referred to design of Whoscall OEM.
 * This class is to handle the sharePreference of the user.
 *
 * */
class PrefsRepository @Inject constructor(module: SharedPrefsModule) {

    val context = module.provideContext()

    private val sharedPreferences = module.provideSharedPreference()
    private var defFactory: IPrefsDefaultValueFactory? = null

    fun setPrefDefaultFactory(factory: IPrefsDefaultValueFactory) {
        this.defFactory = factory
    }

    class PreferenceEditor(private val editor: Editor){
        fun put(key: String, value: String): PreferenceEditor {
            editor.putString(key, value)
            return this
        }

        fun put(key: String, value: Int): PreferenceEditor {
            editor.putInt(key, value)
            return this
        }

        fun put(key: String, value: Long): PreferenceEditor {
            editor.putLong(key, value);
            return this
        }

        fun put(key: String, value: Boolean): PreferenceEditor {
            editor.putBoolean(key, value)
            return this
        }

        fun put(key: String, value: Float): PreferenceEditor {
            editor.putFloat(key, value)
            return this
        }

        fun put(key: String, value: Set<String>): PreferenceEditor {
            editor.putStringSet(key, value)
            return this
        }


    }
    fun getString(key: String, def: String? = null): String? = sharedPreferences.getString(key, def ?: defFactory?.getString(key))


    @JvmOverloads
    fun getInt(key: String, def: Int? = null): Int = sharedPreferences.getInt(key, def ?: defFactory?.getInt(key) ?: throw IllegalStateException("[PrefsRepository] $key need default Int value."))

    @JvmOverloads
    fun getLong(key: String, def: Long? = null): Long = sharedPreferences.getLong(key, def ?: defFactory?.getLong(key) ?: throw IllegalStateException("[PrefsRepository] $key need default Long value."))

    @JvmOverloads
    fun getBoolean(key: String, def: Boolean? = null): Boolean = sharedPreferences.getBoolean(key, def ?: defFactory?.getBoolean(key) ?: throw IllegalStateException("[PrefsRepository] $key need default Boolean value."))


}