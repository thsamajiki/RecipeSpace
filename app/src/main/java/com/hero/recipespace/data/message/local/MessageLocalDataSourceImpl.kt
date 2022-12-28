package com.hero.recipespace.data.message.local

import androidx.lifecycle.asFlow
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.dao.MessageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageLocalDataSource {

    override suspend fun getData(messageKey: String) : MessageData {
        return messageDao.getMessageFromKey(messageKey) ?: error("not found MessageData")
    }

    override fun observeDataList(chatKey: String): Flow<List<MessageData>> {
        return messageDao.getMessages(chatKey).asFlow()
    }

    override suspend fun add(
        messageData: MessageData
    ) {
        messageDao.insertMessage(messageData)
    }

    override suspend fun addAll(messageList: List<MessageData>) {
        messageDao.insertAll(messageList)
    }

    override suspend fun update(
        messageData: MessageData
    ) {
        messageDao.updateMessage(messageData)
    }

    override suspend fun remove(
        messageData: MessageData
    ) {
        messageDao.deleteMessage(messageData)
    }

    override fun clear() {

    }
}