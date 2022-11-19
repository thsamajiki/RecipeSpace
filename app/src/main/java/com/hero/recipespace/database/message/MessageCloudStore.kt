package com.hero.recipespace.database.message

import android.content.Context
import com.hero.recipespace.data.MessageData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class MessageCloudStore : CloudStore<MessageData>() {

    companion object {
        private lateinit var instance : MessageCloudStore

        fun getInstance(context: Context) : MessageCloudStore {
            return instance ?: synchronized(this) {
                instance ?: MessageCloudStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
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