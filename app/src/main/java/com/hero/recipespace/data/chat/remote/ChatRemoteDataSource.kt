package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData

interface ChatRemoteDataSource {
    suspend fun getData(chatKey: String) : ChatData

    suspend fun getDataList(userKey: String) : List<ChatData>

    suspend fun add(chatData: ChatData)

    suspend fun update(chatData: ChatData)

    suspend fun remove(chatData: ChatData)
}