package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatKey: String, message: String) =
        messageRepository.addMessage(chatKey, message)
}