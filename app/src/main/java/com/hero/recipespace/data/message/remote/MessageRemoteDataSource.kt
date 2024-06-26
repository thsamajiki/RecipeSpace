package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.request.ReadMessageDataRequest
import kotlinx.coroutines.flow.Flow

interface MessageRemoteDataSource {
    fun getData(messageKey: String): MessageData

    suspend fun readMessage(request: ReadMessageDataRequest): Boolean

    suspend fun getDataList(chatKey: String): Flow<List<MessageData>>

    suspend fun add(
        chatKey: String,
        message: String,
    ): MessageData

    suspend fun update(
        chatKey: String,
        message: String,
    ): MessageData

    suspend fun remove(
        chatKey: String,
        message: String,
    ): MessageData
}
