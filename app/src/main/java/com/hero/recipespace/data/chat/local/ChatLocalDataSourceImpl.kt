package com.hero.recipespace.data.chat.local

import androidx.lifecycle.asFlow
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.dao.ChatDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatLocalDataSourceImpl @Inject constructor(
    private val chatDao: ChatDao
) : ChatLocalDataSource {

    override suspend fun getData(chatKey: String) : ChatData {
       return chatDao.getChatFromKey(chatKey) ?: error("not found ChatData")
    }

    override fun observeDataList(userKey: String) : Flow<List<ChatData>> {
        return chatDao.getAllChats().asFlow()
    }

    override fun clear() {

    }

    override suspend fun add(chatData: ChatData) {
        chatDao.insertChat(chatData)
    }

    override suspend fun addAll(chatList: List<ChatData>) {
        chatDao.insertAll(chatList)
    }

    override suspend fun update(chatData: ChatData) {
        chatDao.updateChat(chatData)
    }

    override suspend fun remove(chatData: ChatData) {
        chatDao.deleteChat(chatData)
    }
}