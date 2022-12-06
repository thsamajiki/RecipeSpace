package com.hero.recipespace.domain.chat.repository

import com.hero.recipespace.domain.chat.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChat(chatKey: String) : Flow<ChatEntity>

    fun getChatList(userKey: String) : Flow<List<ChatEntity>>

    suspend fun addChat(chatEntity: ChatEntity)

    suspend fun modifyChat(chatEntity: ChatEntity)

    suspend fun deleteChat(chatEntity: ChatEntity)
}