package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.ObserveChatListUseCase
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.util.WLog
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
    application: Application,
    private val getUserUseCase: GetUserUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val observeChatListUseCase: ObserveChatListUseCase
) : AndroidViewModel(application) {

//    private val _chatList = MutableLiveData<List<ChatEntity>>()
//    val chatList2: LiveData<List<ChatEntity>>
//        get() = _chatList

    private val _chatListUiState = MutableLiveData<ChatListUIState>()
    val chatListUiState: LiveData<ChatListUIState>
        get() = _chatListUiState

    val userKey = FirebaseAuth.getInstance().currentUser?.uid
    val chatList: LiveData<List<ChatEntity>> =
        observeChatListUseCase(userKey!!)
            .map {
                it.mapNotNull { chat ->
                    if (chat.userList?.contains(userKey) == true) {
                        chat.copy(
                            displayOtherUserName = {
                                getOtherUserName(chat, userKey)
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