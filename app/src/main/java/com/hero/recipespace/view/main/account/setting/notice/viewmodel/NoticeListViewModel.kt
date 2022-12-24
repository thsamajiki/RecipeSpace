package com.hero.recipespace.view.main.account.setting.notice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.usecase.GetNoticeUseCase
import com.hero.recipespace.domain.notice.usecase.ObserveNoticeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    application: Application,
    private val getNoticeUseCase: GetNoticeUseCase,
    private val observeNoticeListUseCase: ObserveNoticeListUseCase
) : AndroidViewModel(application) {

    companion object {
        const val NOTICE_KEY = "noticeKey"
    }

    private val _notice = MutableLiveData<NoticeEntity>()
    val noticeItem: LiveData<NoticeEntity>
        get() = _notice

//    val notice: LiveData<NoticeEntity> = getNoticeUseCase().asLiveData()
//    val noticeList: LiveData<List<NoticeEntity>> = observeNoticeListUseCase().asLiveData()

//    init {
//        viewModelScope.launch {
//            getNoticeUseCase.invoke(_notice.value!!.key)
//        }
//    }

    fun requestNoticeList() {

        viewModelScope.launch {
            observeNoticeListUseCase.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}