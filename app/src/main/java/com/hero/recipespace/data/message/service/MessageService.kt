package com.hero.recipespace.data.message.service

import com.hero.recipespace.data.message.MessageData

interface MessageService {
    fun getData(messageKey: String) : MessageData

    fun getDataList(userKey: String) : List<MessageData>

    suspend fun add(messageData: MessageData)

    suspend fun update(messageData: MessageData)

    suspend fun remove(messageData: MessageData)
}