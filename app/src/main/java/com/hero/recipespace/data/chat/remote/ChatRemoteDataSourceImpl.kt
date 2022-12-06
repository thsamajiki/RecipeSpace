package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.datastore.ChatCloudStore
import kotlinx.coroutines.flow.Flow

class ChatRemoteDataSourceImpl(
    private val chatCloudStore: ChatCloudStore
) : ChatRemoteDataSource {
    override fun getData(chatKey: String) : Flow<ChatData> {
        return chatCloudStore.getData(chatKey)
    }

    override fun getDataList(
        userKey: String
    ) : Flow<List<ChatData>> {
        return chatCloudStore.getDataList(userKey)
    }

    override suspend fun add(chatData: ChatData) {
        chatCloudStore.add(chatData)
    }

    override suspend fun update(chatData: ChatData) {
        chatCloudStore.update(chatData)
    }

    override suspend fun remove(chatData: ChatData) {
        chatCloudStore.remove(chatData)
    }
}