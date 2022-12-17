package com.hero.recipespace.data.chat.service

import com.google.firebase.firestore.DocumentChange
import com.hero.recipespace.data.chat.ChatData
import kotlinx.coroutines.flow.Flow

interface ChatService {
    suspend fun getData(chatKey: String) : ChatData

    suspend fun getChatByUserKeys(myKey: String, otherUserKey: String) : ChatData

    suspend fun getDataList(userKey: String) : List<ChatData>

    suspend fun observeNewChat(userKey: String): Flow<Pair<DocumentChange.Type, ChatData>>

    suspend fun add(otherUserKey: String,
                    message: String) : ChatData

    suspend fun update(chatData: ChatData)

    suspend fun remove(chatData: ChatData)

    suspend fun checkExistChatData(otherUserKey: String): Boolean
}