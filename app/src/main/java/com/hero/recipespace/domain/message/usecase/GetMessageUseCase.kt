package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend fun invoke(messageKey: String, onCompleteListener: OnCompleteListener<MessageEntity>) {
        messageRepository.getMessage(messageKey, onCompleteListener)
    }
}