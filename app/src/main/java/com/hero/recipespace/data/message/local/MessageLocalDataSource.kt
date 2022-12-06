package com.hero.recipespace.data.message.local

import com.hero.recipespace.data.message.MessageData
import kotlinx.coroutines.flow.Flow

interface MessageLocalDataSource {
    fun getData(messageKey: String) : Flow<MessageData>

    fun getDataList(userKey: String) : Flow<List<MessageData>>

    fun clear()

    suspend fun add(messageData: MessageData)

    suspend fun update(messageData: MessageData)

    suspend fun remove(messageData: MessageData)
}