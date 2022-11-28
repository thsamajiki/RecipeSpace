package com.hero.recipespace.database.message.datastore

import android.content.Context
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.CacheStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class MessageCacheStore: CacheStore<MessageData>() {

    companion object {
        private lateinit var instance : MessageCacheStore

        fun getInstance(context: Context) : MessageCacheStore {
            return instance ?: synchronized(this) {
                instance ?: MessageCacheStore().also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<MessageData>) {
        val chatKey: String = params[0].toString()

        for (messageData in getDataList(object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                TODO("Not yet implemented")
            }
        }))
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<MessageData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }
}