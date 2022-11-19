package com.hero.recipespace.database.chat.datastore

import android.content.Context
import com.hero.recipespace.data.ChatData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class ChatCloudStore : CloudStore<ChatData>() {

    companion object {
        private lateinit var instance : ChatCloudStore

        fun getInstance(context: Context) : ChatCloudStore {
            return instance ?: synchronized(this) {
                instance ?: ChatCloudStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<ChatData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

}