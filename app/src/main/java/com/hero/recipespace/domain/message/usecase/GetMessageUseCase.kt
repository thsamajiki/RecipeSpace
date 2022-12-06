package com.hero.recipespace.domain.message.usecase

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend fun invoke(messageKey: String, onCompleteListener: OnCompleteListener<MessageEntity>) : LiveData<MessageEntity> =
        messageRepository.getMessage(messageKey, onCompleteListener)

}