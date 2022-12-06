package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import javax.inject.Inject

class AddChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatEntity: ChatEntity) =
        chatRepository.addChat(chatEntity)
}