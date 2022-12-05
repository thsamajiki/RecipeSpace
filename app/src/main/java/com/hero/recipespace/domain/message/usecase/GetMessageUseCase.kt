package com.hero.recipespace.domain.message.usecase

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend fun invoke(messageKey: String, onCompleteListener: OnCompleteListener<MessageEntity>) : LiveData<MessageEntity> {
        return messageRepository.getMessage(messageKey, onCompleteListener)
    }
}