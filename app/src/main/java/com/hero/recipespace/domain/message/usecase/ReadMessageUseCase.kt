package com.hero.recipespace.domain.message.usecase

import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.domain.message.request.ReadMessageRequest
import javax.inject.Inject

class ReadMessageUseCase
@Inject
constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(request: ReadMessageRequest) =
        messageRepository.readMessage(request)
}
