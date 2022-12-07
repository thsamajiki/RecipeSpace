package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.service.MessageService

class MessageRemoteDataSourceImpl(
    private val messageService: MessageService
) : MessageRemoteDataSource {

    override fun getData(messageKey: String): MessageData {
        return messageService.getData(messageKey)
    }

    override fun getDataList(
        userKey: String
    ): List<MessageData> {
        return messageService.getDataList(userKey)
    }

    override suspend fun add(
        messageData: MessageData
    ) {
        messageService.add(messageData)
    }

    override suspend fun update(
        messageData: MessageData
    ) {
        messageService.update(messageData)
    }

    override suspend fun remove(
        messageData: MessageData
    ) {
        messageService.remove(messageData)
    }
}