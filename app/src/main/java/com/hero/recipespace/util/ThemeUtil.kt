package com.hero.recipespace.util

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

object ThemeUtil {

    private const val LIGHT_MODE = "light"
    private const val DARK_MODE = "dark"
    const val DEFAULT_MODE = "default" // 시스템 설정에 따르기
    const val THEME_PREF_KEY = "themePref"


    fun applyTheme(theme: String) {
        val mode = when (theme) {
            LIGHT_MODE -> AppCompatDelegate.MODE_NIGHT_NO
            DARK_MODE -> AppCompatDelegate.MODE_NIGHT_YES
            else -> {
                // Android 10 이상은 디폴트로 다크 모드를 지원함
                // 그 이하는 Battery Saver 모드가 시스템을 다크 모드로 전환함
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}