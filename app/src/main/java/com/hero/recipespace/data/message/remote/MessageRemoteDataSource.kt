package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData

interface MessageRemoteDataSource {
    fun getData(messageKey: String) : MessageData

    fun getDataList(userKey: String) : List<MessageData>

    suspend fun add(messageData: MessageData)

    suspend fun update(messageData: MessageData)

    suspend fun remove(messageData: MessageData)
}