package com.hero.recipespace.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object RSPreferences {

    fun prefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

//    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.lightModeState
        get() = getBoolean("light_mode", true)
        set(value) {
            editMe {
                it.putBoolean("light_mode", value)
            }
        }

    var SharedPreferences.darkModeState
        get() = getBoolean("dark_mode", false)
        set(value) {
            editMe {
                it.putBoolean("dark_mode", value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }

//    var SharedPreferences.userId
//        get() = getInt(USER_ID, 0)
//        set(value) {
//            editMe {
//                it.putInt(USER_ID, value)
//            }
//        }
//
//    var SharedPreferences.password
//        get() = getString(USER_PASSWORD, "")
//        set(value) {
//            editMe {
//                it.putString(USER_PASSWORD, value)
//            }
//        }
//

//    fun setLightModeState(state: Boolean) = prefs.edit().putBoolean("LightMode", state).apply()
//
//    fun getLightModeState(): Boolean = prefs.getBoolean("light_mode", false)
//
//    fun setNightModeState(state: Boolean) = prefs.edit().putBoolean("NightMode", state).apply()
//
//    fun getNightModeState(): Boolean = prefs.getBoolean("dark_mode", false)

}