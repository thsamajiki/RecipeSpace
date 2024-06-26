package com.hero.recipespace.view.main.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.request.AddChatRequest
import com.hero.recipespace.domain.chat.usecase.CreateNewChatRoomUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatByRecipeChatInfoUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.request.ReadMessageRequest
import com.hero.recipespace.domain.message.usecase.ObserveMessageListUseCase
import com.hero.recipespace.domain.message.usecase.ReadMessageUseCase
import com.hero.recipespace.domain.message.usecase.SendMessageUseCase
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.main.chat.MessageItem
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import com.hero.recipespace.view.main.chat.toItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
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
class ChatViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatUseCase: GetChatUseCase,
    private val getChatByUserKeyUseCase: GetChatByRecipeChatInfoUseCase,
    private val createNewChatRoomUseCase: CreateNewChatRoomUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val observeMessageListUseCase: ObserveMessageListUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
) : ViewModel() {
    private val recipeChatInfo: RecipeChatInfo? =
        savedStateHandle.get<RecipeChatInfo>(EXTRA_RECIPE_CHAT)

    private val _chatUiState = MutableLiveData<ChatUIState>()
    val chatUiState: LiveData<ChatUIState>
        get() = _chatUiState

    private val _messageUiState = MutableLiveData<MessageUIState>()
    val messageUiState: LiveData<MessageUIState>
        get() = _messageUiState

    private val _chat = MutableLiveData<ChatEntity>()
    val chat: LiveData<ChatEntity>
        get() = _chat

    private val _otherUserName =
        MediatorLiveData<String>().apply {
            value = recipeChatInfo?.userName.orEmpty()

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

    private val _otherUserProfileImage =
        MediatorLiveData<String>().apply {
            value = recipeChatInfo?.userProfileImageUrl.orEmpty()

            addSource(chat) { chatEntity ->
                viewModelScope.launch {
                    val myKey = getLoggedUserUseCase().getOrNull()?.key.orEmpty()
                    val otherUserProfileImage = getOtherUserProfileImage(chatEntity, myKey)

                    if (otherUserProfileImage.isNotEmpty()) {
                        value = otherUserProfileImage
                    }
                }
            }
        }
    val otherUserProfileImage: LiveData<String>
        get() = _otherUserProfileImage

    private val _myUserName = MutableLiveData<String>()
    val myUserName: LiveData<String>
        get() = _myUserName

    private val _myProfileImageUrl = MutableLiveData<String>()
    val myProfileImageUrl: LiveData<String>
        get() = _myProfileImageUrl

    val message: MutableLiveData<String> = MutableLiveData()
    private val _messageList = MutableLiveData<List<MessageItem>>()
    val messageList: LiveData<List<MessageItem>>
        get() = _messageList

    private val _isRead = MutableLiveData<Boolean>()
    val isRead: LiveData<Boolean>
        get() = _isRead

    init {
        val chatKey: String = savedStateHandle.get<String>(CHAT_KEY).orEmpty()

        viewModelScope.launch {
            val chatEntity: ChatEntity? = getChatRoom(chatKey, recipeChatInfo)
            if (chatEntity != null) {
                activateChatRoom(chatEntity)
            }

            _myUserName.value = getMyUserName()
            _myProfileImageUrl.value = getMyProfileImage()
        }
    }

    var count2 = 0

    private suspend fun observeMessage(
        chatKey: String,
        myKey: String,
    ) {
        Log.d("count2", "count2: $count2")
        observeMessageListUseCase(chatKey)
            .flowOn(Dispatchers.Main).distinctUntilChanged()
            .collect { messageList ->
                WLog.d("observeMessage $messageList")

                val unreadMessageList =
                    messageList
                        .filter {
                            it.isRead == false
                        }
                        .filter {
                            it.userKey != myKey
                        }

                readMessage(unreadMessageList)

                _messageList.value =
                    messageList
                        .map { message ->
                            val userName = chat.value?.userNames?.get(message.userKey)
                            message.copy(userName = userName.orEmpty())
                        }
                        .sortedBy {
                            it.timestamp
                        }
                        .map {
                            it.toItem(
                                displayOtherUserProfileImage = {
                                    getOtherUserProfileImage(chat.value!!, myKey)
                                },
                            )
                        }
                Log.d("_messageList.value", "_messageList.value: ${_messageList.value}")
            }
        count2++
    }

    private suspend fun readMessage(unreadMessageList: List<MessageEntity>) {
        val chatKey = chat.value?.key ?: return
        val userKey = getLoggedUserUseCase().getOrNull()?.key ?: return

        readMessageUseCase(
            ReadMessageRequest(
                chatKey,
                unreadMessageList,
                userKey,
            ),
        )
    }

//    private suspend fun observeMessage(chatKey: String, myKey: String) {
//        observeMessageListUseCase(chatKey)
//            .flowOn(Dispatchers.Main)
//            .map { messageList ->
//                messageList
//                    .map { message ->
//                        val userName = chat.value?.userNames?.get(message.userKey)
//                        message.copy(userName = userName.orEmpty())
//                    }
//                    .sortedBy { it.timestamp }
//            }
//            .distinctUntilChanged() // 데이터 변화가 있을 때만 다음 스텝으로 진행
//            .collect { transformedList ->
//                _messageList.value = transformedList.map {
//                    it.toItem(
//                        displayOtherUserProfileImage = {
//                            getOtherUserProfileImage(chat.value!!, myKey)
//                        }
//                    )
//                }
//            }
//    }

    private suspend fun getChatRoom(
        chatKey: String,
        chatInfo: RecipeChatInfo?,
    ): ChatEntity? {
        // chatKey 만 들어올 수도 있고,
        // otherUserKey 만 들어올 수도 있음.

        return if (chatKey.isNotEmpty()) {
            getChatUseCase(chatKey)
                .onFailure {
                    it.printStackTrace()
                    WLog.e(it)
                }
                .getOrNull()
        } else if (chatInfo != null) {
            // identifier
            // 특정 상대방과의 채팅방은, 1개이다.
            // ant1102-userKey - 짜장면(Recipe key) 채팅방
            // ant1102-userKey - 떡볶이(Recipe key) 채팅방
            // ant1102-userKey - 스테이크(Recipe key) 채팅방

            getChatByUserKeyUseCase(chatInfo)
                .onFailure {
                    it.printStackTrace()
                    WLog.e(it)
                }
                .getOrNull()
        } else {
            null
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            if (message != "") {
                val chatKey = chat.value?.key
                if (!chatKey.isNullOrEmpty()) {
                    sendMessageUseCase(
                        chatKey,
                        message,
                    )
                } else if (recipeChatInfo != null) {
                    val chatEntity =
                        createNewChatRoomUseCase(
                            AddChatRequest(
                                recipeChatInfo.userKey,
                                recipeChatInfo.recipeKey,
                                message,
                            ),
                        )
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
            _chat.value = chatEntity
            observeMessage(
                chatEntity.key,
                myKey,
            )
        }
    }

    private fun getOtherUserName(
        chatEntity: ChatEntity,
        myKey: String,
    ) =
        chatEntity.userNames?.toList()
            ?.filterNot {
                it.first == myKey
            }
            ?.firstOrNull()
            ?.second
            .orEmpty()

    private fun getOtherUserProfileImage(
        chatEntity: ChatEntity,
        myKey: String,
    ) =
        chatEntity.userProfileImages?.toList()
            ?.filterNot {
                it.first == myKey
            }
            ?.firstOrNull()
            ?.second
            .orEmpty()

    private suspend fun getMyUserName() =
        getLoggedUserUseCase().getOrNull()?.name.orEmpty()

    private suspend fun getMyProfileImage() =
        getLoggedUserUseCase().getOrNull()?.profileImageUrl.orEmpty()

    companion object {
        const val EXTRA_RECIPE_CHAT = "recipe_chat"
        const val CHAT_KEY = "chatKey"
    }
}
