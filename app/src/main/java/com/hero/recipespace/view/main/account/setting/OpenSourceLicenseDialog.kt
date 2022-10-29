package com.hero.recipespace.view.main.account.setting

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hero.recipespace.R
import com.hero.recipespace.databinding.DialogOpenSourceLicenseBinding

class OpenSourceLicenseDialog(private val context: Context) {

    private lateinit var binding: DialogOpenSourceLicenseBinding

    fun getOpenSourceLicenseDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle("오픈소스 라이센스")
            .setView(R.layout.dialog_open_source_license)
            .setPositiveButton("닫기", null)
            .create()
            .show()
    }
}