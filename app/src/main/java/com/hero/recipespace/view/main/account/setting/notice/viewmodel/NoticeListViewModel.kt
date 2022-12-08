package com.hero.recipespace.view.main.account.setting.notice.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.hero.recipespace.domain.notice.entity.NoticeEntity
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

    companion object {
        const val NOTICE_KEY = "noticeKey"
    }

    private val _notice = MutableLiveData<NoticeEntity>()
    val noticeItem: LiveData<NoticeEntity>
        get() = _notice

    val notice: LiveData<NoticeEntity> = getNoticeUseCase().asLiveData()
    val noticeList: LiveData<List<NoticeEntity>> = getNoticeListUseCase().asLiveData()

    init {
        viewModelScope.launch {
            getNoticeUseCase.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}