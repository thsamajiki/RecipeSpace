package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetChatUseCase(
    private val chatRepository: ChatRepository
) {
    suspend fun invoke(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) {
        chatRepository.getChat(chatKey, onCompleteListener)
    }
}