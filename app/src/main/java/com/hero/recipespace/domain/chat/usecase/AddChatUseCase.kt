package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.repository.ChatRepository
import javax.inject.Inject

class AddChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(otherUserKey: String, message: String) =
        chatRepository.addChat(otherUserKey, message)
}