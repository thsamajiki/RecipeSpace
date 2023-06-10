package com.hero.recipespace.view.main.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.ObserveChatListUseCase
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.main.chat.ChatItem
import com.hero.recipespace.view.main.chat.toItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatListUIState {

    data class Success(val userEntity: UserEntity) : ChatListUIState()

    data class Failed(val message: String) : ChatListUIState()
}

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    observeChatListUseCase: ObserveChatListUseCase
) : ViewModel() {

    private val _chatListUiState = MutableLiveData<ChatListUIState>()
    val chatListUiState: LiveData<ChatListUIState>
        get() = _chatListUiState

    val userKey = FirebaseAuth.getInstance().currentUser?.uid
    val chatList: LiveData<List<ChatItem>> =
        observeChatListUseCase(userKey!!)
            .map { chatList ->
                chatList
                    .mapNotNull { chat ->
                        if (chat.userList?.contains(userKey) == true) {
                            chat.toItem(
                                displayOtherUserName = {
                                    getOtherUserName(chat, userKey) // chat 객체의 상대방 사용자 이름
                                },
                                displayOtherUserProfileImage = {
                                    getOtherUserProfileImage(chat, userKey) // chat 객체의 상대방 사용자 프로필 이미지
                                }
                            )
                        } else {
                            null
                        }
                    }
            }
            .catch { exception ->
                WLog.e(exception)
            }
            .asLiveData()

    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity>
        get() = _user

    val profileImageUrl: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    init {
        getUser()

        viewModelScope.launch {
            getLoggedUserUseCase()
                .onSuccess {
                    _chatListUiState.value = ChatListUIState.Success(it)
                }
                .onFailure {
                    _chatListUiState.value = ChatListUIState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            getUserUseCase(FirebaseAuth.getInstance().uid.orEmpty())
                .onSuccess {
                    _user.value = it
                    profileImageUrl.value = it.profileImageUrl.orEmpty()
                    userName.value = it.name.orEmpty()
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    private fun getOtherUserName(chatEntity: ChatEntity, myKey: String) =
        chatEntity.userNames?.toList() // ChatEntity 의 userNames 맵을 List<Pair<String, String>> 형태로 변환
            ?.filterNot {
                it.first == myKey // 자신의 ID를 필터링. 즉, 자신의 이름을 제외하고, 다른 사용자의 이름만 고려
            }
            ?.firstOrNull() // 첫 번째 나머지 사용자의 Pair 를 선택
            ?.second // 채팅방에서 자신을 제외한 다른 사용자의 이름을 가져옴
            .orEmpty()

    private fun getOtherUserProfileImage(chatEntity: ChatEntity, myKey: String) =
        chatEntity.userProfileImages?.toList()
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