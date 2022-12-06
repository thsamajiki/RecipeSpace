package com.hero.recipespace.view.main.account.setting.notice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.notice.usecase.GetNoticeListUseCase
import com.hero.recipespace.domain.notice.usecase.GetNoticeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    application: Application,
    private val getNoticeUseCase: GetNoticeUseCase,
    private val getNoticeListUseCase: GetNoticeListUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    suspend fun getNotice() {
        getNoticeUseCase.invoke()
    }

    fun getNoticeList() {
        getNoticeListUseCase.invoke()
    }

    override fun onCleared() {
        super.onCleared()
    }
}