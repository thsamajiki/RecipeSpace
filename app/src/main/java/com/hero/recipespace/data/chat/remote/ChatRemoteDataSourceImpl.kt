package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.datastore.ChatCacheStore
import com.hero.recipespace.database.chat.datastore.ChatLocalStore
import com.hero.recipespace.listener.OnCompleteListener

class ChatRemoteDataSourceImpl(
    private val chatLocalStore: ChatLocalStore,
    private val chatCacheStore: ChatCacheStore
) : ChatRemoteDataSource {
    override fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<ChatData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }
}