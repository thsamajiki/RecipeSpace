package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.listener.OnCompleteListener

interface ChatRemoteDataSource {
    suspend fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>)

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatData>>)

    suspend fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    suspend fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    suspend fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)
}