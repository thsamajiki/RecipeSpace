package com.hero.recipespace.data.chat.local

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.listener.OnCompleteListener

class ChatLocalDataSourceImpl : ChatLocalDataSource {
    override fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        chatKey: String,
        onCompleteListener: OnCompleteListener<List<ChatData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun clear() {
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