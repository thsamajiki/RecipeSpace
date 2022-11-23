package com.hero.recipespace.data.chat.local

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.listener.OnCompleteListener

interface ChatLocalDataSource {
    fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>)

    fun getDataList(chatKey: String, onCompleteListener: OnCompleteListener<List<ChatData>>)

    fun clear()

    fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)
}