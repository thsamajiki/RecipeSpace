package com.hero.recipespace.data.chat.local

import com.hero.recipespace.data.chat.ChatData
import kotlinx.coroutines.flow.Flow

interface ChatLocalDataSource {
    suspend fun getData(chatKey: String): ChatData

    fun observeDataList(userKey: String): Flow<List<ChatData>>

    fun clear()

    suspend fun add(chatData: ChatData)

    suspend fun addAll(chatList: List<ChatData>)

    suspend fun update(chatData: ChatData)

    suspend fun remove(chatData: ChatData)
}
