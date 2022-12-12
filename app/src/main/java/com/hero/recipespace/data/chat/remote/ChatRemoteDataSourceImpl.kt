package com.hero.recipespace.data.chat.remote

import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.service.ChatService
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatService: ChatService
) : ChatRemoteDataSource {
    override suspend fun getData(chatKey: String): ChatData {
        return chatService.getData(chatKey)
    }

    override suspend fun getChatByUserKeys(otherUserKey: String): ChatData {
        return chatService.getChatByUserKeys(firebaseAuth.uid.orEmpty(), otherUserKey)
    }

    override suspend fun getDataList(userKey: String): List<ChatData> {
        return chatService.getDataList(userKey)
    }

    override suspend fun add(chatData: ChatData) {
        chatService.add(chatData)
    }

    override suspend fun update(chatData: ChatData) {
        chatService.update(chatData)
    }

    override suspend fun remove(chatData: ChatData) {
        chatService.remove(chatData)
    }
}