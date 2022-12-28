package com.hero.recipespace.data.message.local

import com.hero.recipespace.data.message.MessageData
import kotlinx.coroutines.flow.Flow

interface MessageLocalDataSource {
    suspend fun getData(messageKey: String) : MessageData

    fun observeDataList(chatKey: String) : Flow<List<MessageData>>

    fun clear()

    suspend fun add(messageData: MessageData)

    suspend fun addAll(messageList: List<MessageData>)

    suspend fun update(messageData: MessageData)

    suspend fun remove(messageData: MessageData)
}