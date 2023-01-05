package com.hero.recipespace.domain.chat.repository

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.request.AddChatRequest
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChat(chatKey: String) : ChatEntity

    suspend fun getChatByRecipeChatInfo(recipeChatInfo: RecipeChatInfo) : ChatEntity

    fun observeChatList(userKey: String) : Flow<List<ChatEntity>>

    suspend fun createNewChatRoom(request: AddChatRequest): ChatEntity

    suspend fun modifyChat(chatEntity: ChatEntity)

    suspend fun deleteChat(chatEntity: ChatEntity)
}