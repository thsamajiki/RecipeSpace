package com.hero.recipespace.data.chat.service

import com.hero.recipespace.data.chat.ChatData
import javax.inject.Inject

class ChatServiceImpl @Inject constructor() : ChatService {
    override suspend fun getData(chatKey: String): ChatData {
        TODO("Not yet implemented")
    }

    override suspend fun getDataList(userKey: String): List<ChatData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(chatData: ChatData) {
        TODO("Not yet implemented")
    }

    override suspend fun update(chatData: ChatData) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(chatData: ChatData) {
        TODO("Not yet implemented")
    }
}