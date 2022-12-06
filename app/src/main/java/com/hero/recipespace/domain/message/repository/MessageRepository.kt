package com.hero.recipespace.domain.message.repository

import com.hero.recipespace.domain.message.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessage(messageKey: String) : Flow<MessageEntity>

    fun getMessageList(userKey: String) : Flow<List<MessageEntity>>

    suspend fun addMessage(messageEntity: MessageEntity)

    suspend fun modifyMessage(messageEntity: MessageEntity)

    suspend fun deleteMessage(messageEntity: MessageEntity)
}