package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.CreateNewChatRoomUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatByUserKeyUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.usecase.ObserveMessageListUseCase
import com.hero.recipespace.domain.message.usecase.SendMessageUseCase
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.util.WLog
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
    private val createNewChatRoomUseCase: CreateNewChatRoomUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val observeMessageListUseCase: ObserveMessageListUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
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

    private val _otherUserName = MediatorLiveData<String>().apply {
        addSource(chat) {
            WLog.d("it.userNames ${it.userNames} it.userNames?.toList() ${it.userNames?.toList()}")
            val otherUserName = it.userNames?.toList()?.get(1)?.second.orEmpty()

            value = otherUserName
        }
    }
    val otherUserName: LiveData<String>
        get() = _otherUserName

    val chatKey: String = savedStateHandle.get<String>(CHAT_KEY).orEmpty()
    val otherUserKey: String = savedStateHandle.get<String>(EXTRA_OTHER_USER_KEY).orEmpty()

    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity>
        get() = _user



    // TODO: 2022-12-26 이전에 채팅한 적이 있으면 채팅방을 불러오기
    // TODO: 2022-12-26 이전에 채팅한 적이 없으면 우측 하단의 메시지 전송 버튼을 눌렀을 때 채팅방을 생성하기
    init {
        viewModelScope.launch {
            if (checkNewChatRoom(otherUserKey)) {
                sendMessage(message.value.orEmpty())
            } else {
                getChatRoom()
            }


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

        viewModelScope.launch {
            createNewChatRoomUseCase(otherUserKey, message.value.orEmpty())
        }
    }

    // TODO: 2022-12-26 이전에 채팅한 적이 있는지 없는지 확인하기
    private fun checkNewChatRoom(otherUserKey: String): Boolean {
        if (otherUserKey.isEmpty()) {
            return true
        }

        return false
    }

    // TODO: 2022-12-26 이전에 채팅한 적이 있는 경우에 이미 DB에 저장된 채팅방을 불러오기
    private suspend fun getChatRoom() {
        // chatKey 만 들어올 수도 있고,
        // otherUserKey 만 들어올 수도 있음.

        if (chatKey.isNotEmpty()) {
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

    // TODO: 2022-12-26 이전에 채팅한 적이 없는 경우에 DB에 저장된 채팅방을 생성하기
    fun sendMessage(message: String) {
        viewModelScope.launch {
            sendMessageUseCase.invoke(chatKey, otherUserKey, message)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}