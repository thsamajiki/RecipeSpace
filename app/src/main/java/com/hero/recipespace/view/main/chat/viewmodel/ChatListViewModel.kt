package com.hero.recipespace.view.main.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.chat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    application: Application,
    private val chatRepository: ChatRepository
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}