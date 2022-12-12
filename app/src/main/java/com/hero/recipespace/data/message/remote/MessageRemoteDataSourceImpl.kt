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

    override suspend fun getDataList(
        chatKey: String
    ): List<MessageData> {
        return messageService.getDataList(chatKey)
    }

    override suspend fun add(
        chatKey: String,
        message: String
    ) : MessageData {
        return messageService.add(chatKey, message)
    }

    override suspend fun update(
        chatKey: String,
        messageData: MessageData
    ) : MessageData {
        return messageService.update(chatKey, messageData)
    }

    override suspend fun remove(
        chatKey: String,
        messageData: MessageData
    ) : MessageData {
        return messageService.remove(chatKey, messageData)
    }
}