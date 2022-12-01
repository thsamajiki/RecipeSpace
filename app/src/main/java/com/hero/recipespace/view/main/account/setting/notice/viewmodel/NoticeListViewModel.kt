package com.hero.recipespace.view.main.account.setting.notice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    application: Application,
    private val noticeRepository: NoticeRepository
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}