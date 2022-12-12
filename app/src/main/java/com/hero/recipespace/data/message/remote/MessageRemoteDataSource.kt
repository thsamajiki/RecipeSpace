package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData

interface MessageRemoteDataSource {
    fun getData(messageKey: String) : MessageData

    suspend fun getDataList(chatKey: String) : List<MessageData>

    suspend fun add(chatKey: String, message: String) : MessageData

    suspend fun update(chatKey: String, message: String) : MessageData

    suspend fun remove(chatKey: String, message: String) : MessageData
}