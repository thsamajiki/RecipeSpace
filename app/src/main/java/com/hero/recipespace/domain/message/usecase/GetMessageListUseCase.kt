package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetMessageListUseCase(
    private val messageRepository: MessageRepository
) {
    fun invoke(userKey: String, onCompleteListener: OnCompleteListener<List<MessageEntity>>) {
        messageRepository.getMessageList(userKey, onCompleteListener)
    }
}