package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.service.MessageService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRemoteDataSourceImpl @Inject constructor(
    private val messageService: MessageService
) : MessageRemoteDataSource {

    override fun getData(messageKey: String): MessageData {
        return messageService.getData(messageKey)
    }

    override suspend fun getDataList(
        chatKey: String
    ): Flow<List<MessageData>> {
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
        message: String
    ) : MessageData {
        return messageService.update(chatKey, message)
    }

    override suspend fun remove(
        chatKey: String,
        message: String
    ) : MessageData {
        return messageService.remove(chatKey, message)
    }
}