package com.hero.recipespace.database.chat.datastore

import android.content.Context
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.listener.OnCompleteListener

class ChatLocalStore(
    private val context: Context
): LocalStore<ChatData>(context) {

    companion object {
        private lateinit var instance : ChatLocalStore

        fun getInstance(context: Context) : ChatLocalStore {
            return instance ?: synchronized(this) {
                instance ?: ChatLocalStore(context).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any,
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