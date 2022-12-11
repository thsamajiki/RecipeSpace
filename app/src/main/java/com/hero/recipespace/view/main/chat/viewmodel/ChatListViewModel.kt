package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.ObserveChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    application: Application,
    private val observeChatListUseCase: ObserveChatListUseCase
) : AndroidViewModel(application) {

//    private val _chatList = MutableLiveData<List<ChatEntity>>()
//    val chatList2: LiveData<List<ChatEntity>>
//        get() = _chatList

    val userKey = FirebaseAuth.getInstance().currentUser?.uid
    val chatList: LiveData<List<ChatEntity>> = observeChatListUseCase(userKey!!).asLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}