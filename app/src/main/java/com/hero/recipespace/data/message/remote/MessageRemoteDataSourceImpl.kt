package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.service.MessageService
import javax.inject.Inject

class MessageRemoteDataSourceImpl @Inject constructor(
    private val messageService: MessageService
) : MessageRemoteDataSource {

    override fun getData(messageKey: String): MessageData {
        return messageService.getData(messageKey)
    }

    override fun getDataList(
        chatKey: String
    ): List<MessageData> {
        return messageService.getDataList(chatKey)
    }

    override suspend fun add(
        message: String
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