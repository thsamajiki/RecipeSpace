package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import javax.inject.Inject

class GetMessageUseCase
@Inject
constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(messageKey: String): Result<MessageEntity> =
        kotlin.runCatching {
            messageRepository.getMessage(messageKey)
        }
}
