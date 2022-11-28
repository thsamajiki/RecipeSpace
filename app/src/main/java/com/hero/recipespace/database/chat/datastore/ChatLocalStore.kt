package com.hero.recipespace.database.chat.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.database.chat.dao.ChatDao
import com.hero.recipespace.listener.OnCompleteListener

class ChatLocalStore(
    private val context: Context,
    private val chatDao: ChatDao
): LocalStore<ChatData>(context) {

    companion object {
        private lateinit var instance : ChatLocalStore

        fun getInstance(context: Context, chatDao: ChatDao) : ChatLocalStore {
            return instance ?: synchronized(this) {
                instance ?: ChatLocalStore(context, chatDao).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<ChatData>) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }
        val chatKey: String = params[0].toString()
        val chatData: ChatData = chatDao.getChatFromKey(chatKey)!!
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<ChatData>>
    ) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }

        val chatDataList: LiveData<List<ChatData>> = chatDao.getAllChats()

        onCompleteListener.onComplete(true, chatDataList)
    }

    override fun add(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatDao.insertChat(data)

        onCompleteListener.onComplete(true, data)
    }

    override fun update(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatDao.updateChat(data)

        onCompleteListener.onComplete(true, data)
    }

    override fun remove(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatDao.deleteChat(data)

        onCompleteListener.onComplete(true, data)
    }
}