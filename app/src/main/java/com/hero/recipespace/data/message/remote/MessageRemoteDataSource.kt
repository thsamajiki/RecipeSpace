package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import kotlinx.coroutines.flow.Flow

interface MessageRemoteDataSource {
    fun getData(messageKey: String) : Flow<MessageData>

    fun getDataList(userKey: String) : Flow<List<MessageData>>

    suspend fun add(messageData: MessageData)

    suspend fun update(messageData: MessageData)

    suspend fun remove(messageData: MessageData)
}