package com.hero.recipespace.view.main.account.setting.notice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    application: Application,
    private val noticeRepository: NoticeRepository
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    override fun onCleared() {
        super.onCleared()
    }
}