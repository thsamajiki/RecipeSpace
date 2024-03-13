package com.hero.recipespace.view.main.account.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityThemeSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeSettingActivity :
    AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private lateinit var binding: ActivityThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcvThemeSetting, ThemeSettingFragment())
            .commit()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference,
    ): Boolean {
        return true
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, ThemeSettingActivity::class.java)
    }
}
