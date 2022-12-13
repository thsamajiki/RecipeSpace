package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.repository.MessageRepository
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatKey: String, otherUserKey: String, message: String) =
        messageRepository.addMessage(chatKey, otherUserKey, message)
}