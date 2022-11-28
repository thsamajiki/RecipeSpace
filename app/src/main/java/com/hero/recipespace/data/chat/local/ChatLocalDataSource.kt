package com.hero.recipespace.data.chat.local

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.listener.OnCompleteListener

interface ChatLocalDataSource {
    suspend fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<ChatData>>)

    fun clear()

    suspend fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    suspend fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    suspend fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)
}