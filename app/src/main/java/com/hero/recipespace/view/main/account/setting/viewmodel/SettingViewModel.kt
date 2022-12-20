package com.hero.recipespace.view.main.account.setting.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}