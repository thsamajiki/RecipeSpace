package com.hero.recipespace.domain.message.usecase

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetMessageListUseCase(
    private val messageRepository: MessageRepository
) {
    fun invoke(userKey: String, onCompleteListener: OnCompleteListener<List<MessageEntity>>) : LiveData<List<MessageEntity>> {
        return messageRepository.getMessageList(userKey, onCompleteListener)
    }
}