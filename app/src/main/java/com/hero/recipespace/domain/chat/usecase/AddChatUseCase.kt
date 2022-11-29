package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.listener.OnCompleteListener

class AddChatUseCase(
    private val chatRepository: ChatRepository
) {
    suspend fun invoke(chatEntity: ChatEntity, onCompleteListener: OnCompleteListener<ChatEntity>) {
        chatRepository.addChat(chatEntity, onCompleteListener)
    }
}