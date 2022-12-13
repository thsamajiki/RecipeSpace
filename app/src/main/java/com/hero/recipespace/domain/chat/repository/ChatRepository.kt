package com.hero.recipespace.domain.chat.repository

import com.hero.recipespace.domain.chat.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChat(chatKey: String) : ChatEntity

    suspend fun getChatByUserKeys(otherUserKey: String) : ChatEntity

    fun observeChatList(userKey: String) : Flow<List<ChatEntity>>

    suspend fun addChat(otherUserKey: String, message: String)

    suspend fun modifyChat(chatEntity: ChatEntity)

    suspend fun deleteChat(chatEntity: ChatEntity)
}