package com.hero.recipespace.data.message.service

import com.hero.recipespace.data.message.MessageData
import javax.inject.Inject

class MessageServiceImpl @Inject constructor() : MessageService {
    override fun getData(messageKey: String): MessageData {
        TODO("Not yet implemented")
    }

    override fun getDataList(userKey: String): List<MessageData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(messageData: MessageData) {
        TODO("Not yet implemented")
    }

    override suspend fun update(messageData: MessageData) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(messageData: MessageData) {
        TODO("Not yet implemented")
    }
}