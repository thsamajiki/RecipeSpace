package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetChatListUseCase(
    private val chatRepository: ChatRepository
) {
    fun invoke(userKey: String, onCompleteListener: OnCompleteListener<List<ChatEntity>>) {
        chatRepository.getChatList(userKey, onCompleteListener)
    }
}