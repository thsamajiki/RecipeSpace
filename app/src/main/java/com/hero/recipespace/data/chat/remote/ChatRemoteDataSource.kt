package com.hero.recipespace.data.chat.remote

import com.hero.recipespace.data.chat.ChatData
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {
    fun getData(chatKey: String) : Flow<ChatData>

    fun getDataList(userKey: String) : Flow<List<ChatData>>

    suspend fun add(chatData: ChatData)

    suspend fun update(chatData: ChatData)

    suspend fun remove(chatData: ChatData)
}