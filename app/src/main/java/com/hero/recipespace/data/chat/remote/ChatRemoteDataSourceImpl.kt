package com.hero.recipespace.data.chat.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.service.ChatService
import com.hero.recipespace.domain.chat.request.AddChatRequest
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRemoteDataSourceImpl
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatService: ChatService,
) : ChatRemoteDataSource {
    override suspend fun getData(chatKey: String): ChatData {
        return chatService.getData(chatKey)
    }

    override suspend fun getChatByRecipeChatInfo(recipeChatInfo: RecipeChatInfo): ChatData {
        return chatService.getChatByRecipeChatInfo(firebaseAuth.uid.orEmpty(), recipeChatInfo)
    }

    override suspend fun getDataList(userKey: String): List<ChatData> {
        return chatService.getDataList(userKey)
    }

    override suspend fun observeNewChat(userKey: String): Flow<Pair<DocumentChange.Type, ChatData>> {
        return chatService.observeNewChat(userKey)
    }

    override suspend fun createNewChatRoom(request: AddChatRequest): ChatData {
        return chatService.add(request)
    }

    override suspend fun update(chatData: ChatData) {
        chatService.update(chatData)
    }

    override suspend fun remove(chatData: ChatData) {
        chatService.remove(chatData)
    }

    override suspend fun checkExistChatData(otherUserKey: String): Boolean {
        return chatService.checkExistChatData(otherUserKey)
    }
}
