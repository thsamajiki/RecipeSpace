package com.hero.recipespace.database.chat.datastore

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.dao.ChatDao

class ChatLocalStore(
    private val chatDao: ChatDao
) {

    companion object {
        private lateinit var instance : ChatLocalStore

        fun getInstance(chatDao: ChatDao) : ChatLocalStore {
            return synchronized(this) {
                instance ?: ChatLocalStore(chatDao).also {
                    instance = it
                }
            }
        }
    }

    fun getData(chatKey: String) {
        val chatData: LiveData<ChatData> = chatDao.getChatFromKey(chatKey)
    }

    fun getDataList() {
        val chatDataList: LiveData<List<ChatData>> = chatDao.getAllChats()

    }

    fun add(data: ChatData) {
        chatDao.insertChat(data)

    }

    fun update(data: ChatData) {
        chatDao.updateChat(data)
    }

    fun remove(data: ChatData) {
        chatDao.deleteChat(data)
    }
}