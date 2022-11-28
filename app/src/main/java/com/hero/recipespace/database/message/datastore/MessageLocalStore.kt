package com.hero.recipespace.database.message.datastore

import android.content.Context
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.listener.OnCompleteListener

class MessageLocalStore(
    private val context: Context
) : LocalStore<MessageData>(context) {

    companion object {
        private lateinit var instance : MessageLocalStore

        fun getInstance(context: Context) : MessageLocalStore {
            return instance ?: synchronized(this) {
                instance ?: MessageLocalStore(context).also {
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