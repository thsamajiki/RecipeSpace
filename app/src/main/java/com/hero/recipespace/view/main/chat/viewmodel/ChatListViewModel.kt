package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.usecase.GetChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    application: Application,
    private val getChatListUseCase: GetChatListUseCase
) : AndroidViewModel(application) {

    val userKey = FirebaseAuth.getInstance().currentUser?.uid
    val chatList: LiveData<List<ChatEntity>> = getChatListUseCase(userKey!!).asLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}