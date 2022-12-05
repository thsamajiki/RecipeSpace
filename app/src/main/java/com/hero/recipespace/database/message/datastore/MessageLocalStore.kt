package com.hero.recipespace.database.message.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.database.message.dao.MessageDao
import com.hero.recipespace.listener.OnCompleteListener

class MessageLocalStore(
    private val context: Context,
    private val messageDao: MessageDao
) : LocalStore<MessageData>(context) {

    companion object {
        private lateinit var instance : MessageLocalStore

        fun getInstance(context: Context, messageDao: MessageDao) : MessageLocalStore {
            return instance ?: synchronized(this) {
                instance ?: MessageLocalStore(context, messageDao).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<MessageData>) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }

        val messageKey: String = params[0].toString()
        val messageData: MessageData = messageDao.getMessageFromKey(messageKey)!!
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<MessageData>>,
    ) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }

        val messageDataList: LiveData<List<MessageData>> = messageDao.getAllMessages()

        onCompleteListener.onComplete(true, messageDataList)
    }

    override suspend fun add(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        messageDao.insertMessage(data)

        onCompleteListener.onComplete(true, data)
    }

    override suspend fun update(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        messageDao.updateMessage(data)

        onCompleteListener.onComplete(true, data)
    }

    override suspend fun remove(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        messageDao.deleteMessage(data)

        onCompleteListener.onComplete(true, data)
    }
}