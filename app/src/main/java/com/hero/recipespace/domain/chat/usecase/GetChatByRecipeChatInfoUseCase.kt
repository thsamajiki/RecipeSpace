package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import javax.inject.Inject

class GetChatByRecipeChatInfoUseCase
@Inject
constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatInfo: RecipeChatInfo): Result<ChatEntity> =
        kotlin.runCatching {
            chatRepository.getChatByRecipeChatInfo(chatInfo)
        }
}
