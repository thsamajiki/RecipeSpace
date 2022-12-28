package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
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
import com.hero.recipespace.view.main.chat.OtherUserInfo
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
    private val savedStateHandle: SavedStateHandle,
    private val getChatUseCase: GetChatUseCase,
    private val getChatByUserKeyUseCase: GetChatByUserKeyUseCase,
    private val createNewChatRoomUseCase: CreateNewChatRoomUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val observeMessageListUseCase: ObserveMessageListUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
) : AndroidViewModel(application) {

    companion object {
        const val EXTRA_OTHER_USER = "otherUser"
        const val CHAT_KEY = "chatKey"
    }

    private val otherUserInfo: OtherUserInfo? =
        savedStateHandle.get<OtherUserInfo>(EXTRA_OTHER_USER)

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
        value = otherUserInfo?.name.orEmpty()

        addSource(chat) { chatEntity ->
            viewModelScope.launch {
                val myKey = getLoggedUserUseCase().getOrNull()?.key.orEmpty()
                val otherUserName = getOtherUserName(chatEntity, myKey)

                if (otherUserName.isNotEmpty()) {
                    value = otherUserName
                }
            }
        }
    }
    val otherUserName: LiveData<String>
        get() = _otherUserName

    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity>
        get() = _user

    val message: MutableLiveData<String> = MutableLiveData()
    private val _messageList = MutableLiveData<List<MessageEntity>>()
    val messageList: LiveData<List<MessageEntity>>
        get() = _messageList


    // TODO: 2022-12-26 이전에 채팅한 적이 있으면 채팅방을 불러오기
    // TODO: 2022-12-26 이전에 채팅한 적이 없으면 우측 하단의 메시지 전송 버튼을 눌렀을 때 채팅방을 생성하기
    init {
        val chatKey: String = savedStateHandle.get<String>(CHAT_KEY).orEmpty()
        val otherUserKey: String = otherUserInfo?.key.orEmpty()

        viewModelScope.launch {
            val chatEntity: ChatEntity? = getChatRoom(chatKey, otherUserKey)
            if (chatEntity != null) {
                activateChatRoom(chatEntity)
            }
        }
    }

    private suspend fun observeMessage(chatKey: String) {
        observeMessageListUseCase(chatKey)
            .flowOn(Dispatchers.Main)
            .collect { messageList ->
                WLog.d("observeMessage $messageList")
                _messageList.value = messageList.map { message ->
                    val userName = chat.value?.userNames?.get(message.userKey)
                    message.copy(userName = userName.orEmpty())
                }
                    .sortedBy {
                        it.timestamp
                    }
            }
    }

    private suspend fun getChatRoom(chatKey: String, otherUserKey: String): ChatEntity? {
        // chatKey 만 들어올 수도 있고,
        // otherUserKey 만 들어올 수도 있음.

        return if (chatKey.isNotEmpty()) {
            getChatUseCase(chatKey)
                .onFailure {
                    it.printStackTrace()
                    WLog.e(it)
                }
                .getOrNull()
        } else if (otherUserKey.isNotEmpty()) {
            getChatByUserKeyUseCase(otherUserKey)
                .onFailure {
                    it.printStackTrace()
                    WLog.e(it)
                }
                .getOrNull()
        } else null
    }

    // TODO: 2022-12-26 이전에 채팅한 적이 없는 경우에 DB에 저장된 채팅방을 생성하기
    // TODO: 2022-12-26 메시지 내용을 입력하지 않고 그냥 '보내기' 버튼을 누르면 Toast 메시지 띄우고 메시지를 보내지 않도록 하기
    fun sendMessage(message: String) {
        viewModelScope.launch {
            if (message != "") {
                val chatKey = chat.value?.key
                if (!chatKey.isNullOrEmpty()) {
                    sendMessageUseCase(chatKey, message)
                } else {
                    val otherUserKey: String = otherUserInfo?.key.orEmpty()
                    val chatEntity = createNewChatRoomUseCase(otherUserKey, message)
                    activateChatRoom(chatEntity)
                }
            } else {
                return@launch
            }
        }
    }

    private suspend fun activateChatRoom(chatEntity: ChatEntity) {
        viewModelScope.launch {
            val myKey = getLoggedUserUseCase().getOrNull()?.key.orEmpty()
            _chat.value = chatEntity.copy(
                displayOtherUserName = {
                    getOtherUserName(chatEntity, myKey).apply {
                        WLog.d("getOtherUserName $this")
                    }
                }
            )
            observeMessage(chatEntity.key)
        }
    }

    private fun getOtherUserName(chatEntity: ChatEntity, myKey: String) =
        chatEntity.userNames?.toList()
            ?.filterNot {
                it.first == myKey
            }
            ?.firstOrNull()
            ?.second
            .orEmpty()

    override fun onCleared() {
        super.onCleared()
    }
}