package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.GetChatByUserKeyUseCase
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.usecase.AddMessageUseCase
import com.hero.recipespace.domain.message.usecase.ObserveMessageListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getChatByUserKeyUseCase: GetChatByUserKeyUseCase,
    private val observeMessageListUseCase: ObserveMessageListUseCase,
    private val addMessageUseCase: AddMessageUseCase
) : AndroidViewModel(application) {

    companion object {
        const val EXTRA_OTHER_USER_KEY = "otherUserKey"
        const val RECIPE_CHAT_KEY = "chatKey"
    }

    private val _chat = MutableLiveData<ChatEntity>()
    val chat: LiveData<ChatEntity>
        get() = _chat

    val chatKey: String = savedStateHandle.get<String>(RECIPE_CHAT_KEY).orEmpty()
    val otherUserKey: String = savedStateHandle.get<String>(EXTRA_OTHER_USER_KEY).orEmpty()

    init {
        viewModelScope.launch {
            getChatByUserKeyUseCase(otherUserKey)
                .onSuccess {
                    _chat.value = it
                }.onFailure {
                    it.printStackTrace()
                    Log.e("ChatViewModel", ": $it")
                }

            observeMessageListUseCase(chatKey)
                .flowOn(Dispatchers.Main)
                .collect {
                    _messageList.value = it
                }
        }
    }

    private val _messageList = MutableLiveData<List<MessageEntity>>()
    val messageList: LiveData<List<MessageEntity>>
        get() = _messageList

    override fun onCleared() {
        super.onCleared()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            addMessageUseCase.invoke(message)
        }
    }
}