package com.hero.recipespace

import android.app.Application
import androidx.preference.PreferenceManager
import com.hero.recipespace.util.ThemeUtil
import com.hero.recipespace.util.ThemeUtil.THEME_PREF_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeSpaceApp : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this

        applyTheme()
    }

    private fun applyTheme() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        prefs.getString(THEME_PREF_KEY, ThemeUtil.DEFAULT_MODE)?.apply {
            ThemeUtil.applyTheme(this)
        }
    }

    companion object {
        lateinit var instance: RecipeSpaceApp

        fun getInstance(): Application {
            return instance
        }
    }
}
