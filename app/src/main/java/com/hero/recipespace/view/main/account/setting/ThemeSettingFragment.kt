package com.hero.recipespace.view.main.account.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.hero.recipespace.R
import com.hero.recipespace.util.ThemeUtil

class ThemeSettingFragment :
    PreferenceFragmentCompat() {
    private lateinit var prefs: SharedPreferences
//    private lateinit var prefListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?,
    ) {
        // preference xml 을 inflate 하는 메서드
        setPreferencesFromResource(R.xml.theme_setting_pref, rootKey)

        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        setSharedPreferenceChangeListeners()
    }

    private fun setSharedPreferenceChangeListeners() {
        findPreference<ListPreference>(ThemeUtil.THEME_PREF_KEY)?.apply {
            onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    ThemeUtil.applyTheme(newValue as String)
                    true
                }
        }
    }
}
