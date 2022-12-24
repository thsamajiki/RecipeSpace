package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.GetChatByUserKeyUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.usecase.SendMessageUseCase
import com.hero.recipespace.domain.message.usecase.ObserveMessageListUseCase
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatUIState {

    data class Success(val chatEntity: ChatEntity) : ChatUIState()

    data class Failed(val message: String) : ChatUIState()
}

sealed class MessageUIState {

    data class Success(val messageEntity: MessageEntity) : MessageUIState()

    data class Failed(val message: String) : MessageUIState()
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getChatUseCase: GetChatUseCase,
    private val getChatByUserKeyUseCase: GetChatByUserKeyUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val observeMessageListUseCase: ObserveMessageListUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : AndroidViewModel(application) {

    companion object {
        const val EXTRA_OTHER_USER_KEY = "otherUserKey"
        const val CHAT_KEY = "chatKey"
    }

    private val _chatUiState = MutableLiveData<ChatUIState>()
    val chatUiState: LiveData<ChatUIState>
        get() = _chatUiState

    private val _messageUiState = MutableLiveData<MessageUIState>()
    val messageUiState: LiveData<MessageUIState>
        get() = _messageUiState

    private val _chat = MutableLiveData<ChatEntity>()
    val chat: LiveData<ChatEntity>
        get() = _chat

    val chatKey: String = savedStateHandle.get<String>(CHAT_KEY).orEmpty()
    val otherUserKey: String = savedStateHandle.get<String>(EXTRA_OTHER_USER_KEY).orEmpty()

    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity>
        get() = _user

    init {
        viewModelScope.launch {
            getChatRoom()

            observeMessageListUseCase(chatKey)
                .flowOn(Dispatchers.Main)
                .collect {
                    _messageList.value = it
                }
        }

        viewModelScope.launch {
            getChatUseCase(chatKey)
                .onSuccess {
                    _chatUiState.value = ChatUIState.Success(it)
                }
                .onFailure {
                    _chatUiState.value = ChatUIState.Failed(it.message.orEmpty())
                }
        }
    }

    private suspend fun getChatRoom() {
        // chatKey 만 들어올 수도 있고,
        // otherUserKey 만 들어올 수도 있음.

        if(chatKey.isNotEmpty()) {
            getChatUseCase(chatKey)
                .onSuccess {
                    _chat.value = it
                }
                .onFailure {
                    it.printStackTrace()
                    Log.e("ChatViewModel", ": $it")
                }
        } else if (otherUserKey.isNotEmpty()) {
            getChatByUserKeyUseCase(otherUserKey)
                .onSuccess {
                    _chat.value = it
                }.onFailure {
                    it.printStackTrace()
                    Log.e("ChatViewModel", ": $it")
                }
        }
    }

    val message: MutableLiveData<String> = MutableLiveData()
    private val _messageList = MutableLiveData<List<MessageEntity>>()
    val messageList: LiveData<List<MessageEntity>>
        get() = _messageList

    fun sendMessage(message: String) {
        viewModelScope.launch {
            sendMessageUseCase.invoke(chatKey, otherUserKey, message)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}