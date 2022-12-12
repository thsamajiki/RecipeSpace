package com.hero.recipespace.data.message.service

import com.hero.recipespace.data.message.MessageData

interface MessageService {
    fun getData(messageKey: String) : MessageData

    fun getDataList(chatKey: String) : List<MessageData>

    suspend fun add(message: String) : MessageData

    suspend fun update(messageData: MessageData)

    suspend fun remove(messageData: MessageData)
}