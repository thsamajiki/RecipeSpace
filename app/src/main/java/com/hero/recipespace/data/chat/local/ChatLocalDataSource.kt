package com.hero.recipespace.data.chat.local

import com.hero.recipespace.data.chat.ChatData
import kotlinx.coroutines.flow.Flow

interface ChatLocalDataSource {
    fun getData(chatKey: String) : Flow<ChatData>

    fun getDataList(userKey: String) : Flow<List<ChatData>>

    fun clear()

    suspend fun add(chatData: ChatData)

    suspend fun update(chatData: ChatData)

    suspend fun remove(chatData: ChatData)
}