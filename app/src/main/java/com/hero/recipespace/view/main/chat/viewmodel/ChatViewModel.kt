package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.usecase.AddMessageUseCase
import com.hero.recipespace.domain.message.usecase.ObserveMessageListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getChatUseCase: GetChatUseCase,
    private val observeMessageListUseCase: ObserveMessageListUseCase,
    private val addMessageUseCase: AddMessageUseCase
) : AndroidViewModel(application) {

    companion object {
        const val CHAT_KEY = "key"
    }

    private val _chat = MutableLiveData<ChatEntity>()
    val chat: LiveData<ChatEntity>
        get() = _chat

    val chatKey: String = savedStateHandle.get<String>(CHAT_KEY)!!

    init {
        viewModelScope.launch {
            getChatUseCase(chatKey)
                .onSuccess {
                    _chat.value = it
                }.onFailure {
                    it.printStackTrace()
                    Log.e("ChatViewModel", ": $it")
                }

            observeMessageListUseCase
        }
    }

    private val _messageList = MutableLiveData<List<MessageEntity>>()
    val messageList: LiveData<List<MessageEntity>>
        get() = _messageList

    suspend fun getChat() {
        getChatUseCase.invoke(chatKey)
    }

    suspend fun addMessage(messageEntity: MessageEntity) {
        addMessageUseCase.invoke(messageEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}