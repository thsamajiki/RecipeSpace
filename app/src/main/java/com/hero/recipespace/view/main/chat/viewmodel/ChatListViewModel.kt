package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.chat.usecase.GetChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    application: Application,
    private val getChatListUseCase: GetChatListUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    fun getChatList() {

    }

    override fun onCleared() {
        super.onCleared()
    }
}