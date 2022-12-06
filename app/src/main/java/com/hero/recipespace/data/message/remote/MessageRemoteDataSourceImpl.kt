package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.datastore.MessageCloudStore
import kotlinx.coroutines.flow.Flow

class MessageRemoteDataSourceImpl(
    private val messageCloudStore: MessageCloudStore
) : MessageRemoteDataSource {

    override fun getData(messageKey: String): Flow<MessageData> {
        return messageCloudStore.getData(messageKey)
    }

    override fun getDataList(
        userKey: String
    ): Flow<List<MessageData>> {
        return messageCloudStore.getDataList()
    }

    override suspend fun add(
        messageData: MessageData
    ) {
        messageCloudStore.add(messageData)
    }

    override suspend fun update(
        messageData: MessageData
    ) {
        messageCloudStore.update(messageData)
    }

    override suspend fun remove(
        messageData: MessageData
    ) {
        messageCloudStore.remove(messageData)
    }
}