package com.hero.recipespace.domain.message.repository

import com.hero.recipespace.domain.message.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessage(messageKey: String) : MessageEntity

    fun getMessageList(chatKey: String) : Flow<List<MessageEntity>>

    suspend fun addMessage(message: String)

    suspend fun modifyMessage(messageEntity: MessageEntity)

    suspend fun deleteMessage(messageEntity: MessageEntity)
}