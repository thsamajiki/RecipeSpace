package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.repository.ChatRepository
import javax.inject.Inject

class CountUnreadMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
//    suspend operator fun invoke(chatKey: String) =
//        chatRepository.readMessage(chatKey)
}