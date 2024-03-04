package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.domain.chat.request.AddChatRequest
import javax.inject.Inject

class CreateNewChatRoomUseCase
@Inject
constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(request: AddChatRequest) =
        chatRepository.createNewChatRoom(request)
}
