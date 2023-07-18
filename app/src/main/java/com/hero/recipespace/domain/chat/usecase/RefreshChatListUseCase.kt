package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.repository.ChatRepository
import javax.inject.Inject

class RefreshChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(userKey: String): Result<Unit> {
        return kotlin.runCatching { chatRepository.refresh(userKey) }
    }
}