package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.datastore.ChatCloudStore
import kotlinx.coroutines.flow.Flow

class ChatRemoteDataSourceImpl(

) : ChatRemoteDataSource {
    override suspend fun getData(chatKey: String): ChatData {
        TODO("Not yet implemented")
    }

    override suspend fun getDataList(userKey: String): List<ChatData> {
        TODO("Not yet implemented")
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