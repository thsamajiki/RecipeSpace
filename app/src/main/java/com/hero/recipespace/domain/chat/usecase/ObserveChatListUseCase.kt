package com.hero.recipespace.domain.chat.usecase

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatListUseCase
@Inject
constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(userKey: String): Flow<List<ChatEntity>> =
        chatRepository.observeChatList(userKey)
}
