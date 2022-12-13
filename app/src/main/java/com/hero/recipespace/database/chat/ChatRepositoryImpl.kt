package com.hero.recipespace.database.chat

import com.google.firebase.firestore.DocumentChange
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.local.ChatLocalDataSource
import com.hero.recipespace.data.chat.remote.ChatRemoteDataSource
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.mapper.toData
import com.hero.recipespace.domain.chat.mapper.toEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource
) : ChatRepository {

    override suspend fun getChat(chatKey: String) : ChatEntity {
        return chatRemoteDataSource.getData(chatKey).toEntity()
    }

    override suspend fun getChatByUserKeys(otherUserKey: String): ChatEntity {
        return chatRemoteDataSource.getChatByUserKeys(otherUserKey).toEntity()
    }

    override fun observeChatList(
        userKey: String
    ) : Flow<List<ChatEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            chatRemoteDataSource.observeNewChat(userKey)
                .collect { (type, chatData) ->
                    when(type) {
                        DocumentChange.Type.ADDED -> chatLocalDataSource.add(chatData)
                        DocumentChange.Type.MODIFIED -> chatLocalDataSource.update(chatData)
                        DocumentChange.Type.REMOVED -> chatLocalDataSource.remove(chatData)
                    }
                }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val chatList = chatRemoteDataSource.getDataList(userKey)
            chatLocalDataSource.addAll(chatList)
            cancel()
        }

        return chatLocalDataSource.observeDataList(userKey)
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addChat(otherUserKey: String,
                                 message: String) {
        if (!chatRemoteDataSource.checkExistChatData(otherUserKey)) {
            val result = chatRemoteDataSource.add(otherUserKey, message)
            chatLocalDataSource.add(result)
        }
    }

    override suspend fun modifyChat(
        chatEntity: ChatEntity
    ) {
        chatRemoteDataSource.update(chatEntity.toData())
        chatLocalDataSource.update(chatEntity.toData())
    }

    override suspend fun deleteChat(
        chatEntity: ChatEntity
    ) {
        chatRemoteDataSource.remove(chatEntity.toData())
        chatLocalDataSource.remove(chatEntity.toData())
    }

    private fun getEntities(data: List<ChatData>): List<ChatEntity> {
        val result = mutableListOf<ChatEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}