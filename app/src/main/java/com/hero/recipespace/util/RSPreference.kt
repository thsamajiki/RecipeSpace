package com.hero.recipespace.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RSPreference(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(KEY, 0)
    private var editor: SharedPreferences.Editor = prefs.edit()

    fun getStringArray(key: String): MutableList<String> {
        val gson = Gson()
        val arrayType = object : TypeToken<MutableList<String>>() {}.type
        val json = prefs.getString(key, null)
        return if (json != null) gson.fromJson(json, arrayType)
        else ArrayList()
    }

    fun setStringArray(key: String, values: MutableList<String>) {
        val gson = Gson()
        val str = gson.toJson(values)
        if (values.isNotEmpty()) {
            editor.putString(key, str)
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getString(key: String, defValue: String?): String? {
        return prefs.getString(key, defValue)
    }

    fun setString(key: String, str: String?) {
        editor.putString(key, str).apply()
    }

    fun getInt(key: String, int: Int): Int {
        return prefs.getInt(key, int)
    }

    fun setInt(key: String, int: Int) {
        editor.putInt(key, int).commit()
    }

    fun getBoolean(key: String, boolean: Boolean): Boolean {
        return prefs.getBoolean(key, boolean)
    }

    fun setBoolean(key: String, boolean: Boolean) {
        editor.putBoolean(key, boolean).apply()
    }

    fun setObject(key: String, objectData : Any){
        val gson = Gson()
        val value = gson.toJson(objectData)
        editor.putString(key, value).apply()
    }

    fun clear() {
        editor.clear().apply()
    }

    fun remove(key: String) {
        editor.remove(key).apply()
    }

    companion object {
        const val KEY = "SharedPreference"

        const val SETTING_FILE_NAME = "app_setting.pref"

        const val KEY_THEME_ID = "theme_id"
        const val KEY_FONT = "font"
        const val KEY_PERIODIC_SYNC_ENABLED = "periodic_sync_enabled"
        const val KEY_SYNC_PERIOD_IN_MILLS = "sync_period_in_mills"
        const val KEY_DEFAULT_SIGNATURE = "default_signature"
    }
}