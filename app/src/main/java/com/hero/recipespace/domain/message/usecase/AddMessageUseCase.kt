package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener

class AddMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend fun invoke(messageEntity: MessageEntity, onCompleteListener: OnCompleteListener<MessageEntity>) {
        messageRepository.addMessage(messageEntity, onCompleteListener)
    }
}