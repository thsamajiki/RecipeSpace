package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.datastore.ChatCloudStore
import com.hero.recipespace.listener.OnCompleteListener

class ChatRemoteDataSourceImpl(
    private val chatCloudStore: ChatCloudStore
) : ChatRemoteDataSource {
    override fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>) {
        chatCloudStore.getData(chatKey, onCompleteListener)
    }

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<ChatData>>
    ) {
        chatCloudStore.getDataList(userKey, onCompleteListener)
    }

    override fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatCloudStore.add(chatData, onCompleteListener)
    }

    override fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatCloudStore.update(chatData, onCompleteListener)
    }

    override fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatCloudStore.remove(chatData, onCompleteListener)
    }
}