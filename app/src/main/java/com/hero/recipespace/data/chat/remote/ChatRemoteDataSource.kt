package com.hero.recipespace.data.chat.remote

import com.google.firebase.firestore.DocumentChange
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.domain.chat.request.AddChatRequest
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {
    suspend fun getData(chatKey: String): ChatData

    suspend fun getChatByRecipeChatInfo(recipeChatInfo: RecipeChatInfo): ChatData

    suspend fun getDataList(userKey: String): List<ChatData>

    suspend fun observeNewChat(userKey: String): Flow<Pair<DocumentChange.Type, ChatData>>

    suspend fun createNewChatRoom(request: AddChatRequest): ChatData

    suspend fun update(chatData: ChatData)

    suspend fun remove(chatData: ChatData)

    suspend fun checkExistChatData(otherUserKey: String): Boolean
}
