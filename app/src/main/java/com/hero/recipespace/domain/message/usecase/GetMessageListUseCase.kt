package com.hero.recipespace.domain.message.usecase

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class GetMessageListUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    fun invoke(userKey: String, onCompleteListener: OnCompleteListener<List<MessageEntity>>) : LiveData<List<MessageEntity>> =
        messageRepository.getMessageList(userKey, onCompleteListener)

}