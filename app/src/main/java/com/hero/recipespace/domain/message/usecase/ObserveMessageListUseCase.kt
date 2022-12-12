package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessageListUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatKey: String) : Flow<List<MessageEntity>> =
        messageRepository.getMessageList(chatKey)
}