package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.usecase.AddMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    private val getChatUseCase: GetChatUseCase,
    private val addMessageUseCase: AddMessageUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    suspend fun getChat() {
        getChatUseCase.invoke()
    }

    suspend fun addMessage(messageEntity: MessageEntity) {
        addMessageUseCase.invoke(messageEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}