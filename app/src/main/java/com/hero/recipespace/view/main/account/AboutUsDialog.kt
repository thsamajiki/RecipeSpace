package com.hero.recipespace.view.main.account

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hero.recipespace.BuildConfig
import com.hero.recipespace.R
import com.hero.recipespace.databinding.DialogAboutUsBinding

class AboutUsDialog(private val context: Context) {
    private lateinit var binding: DialogAboutUsBinding

    fun getAboutUsDialog() {
        //TextView tvVersion = findViewById(R.id.tv_version);
//        val version: String = BuildConfig.VERSION_NAME
        //tvVersion.setText(version);
        MaterialAlertDialogBuilder(context)
            .setTitle("앱의 정보")
            .setView(R.layout.dialog_about_us)
            .setPositiveButton("닫기", null)
            .create()
            .show()
    }
}