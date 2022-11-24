package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.listener.OnCompleteListener

interface ChatRemoteDataSource {
    fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>)

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatData>>)

    fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)
}