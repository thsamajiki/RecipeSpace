package com.hero.recipespace.database.chat

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

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource
) : ChatRepository {

    override suspend fun getChat(chatKey: String) : ChatEntity {
        return chatLocalDataSource.getData(chatKey).toEntity()
    }

    override fun observeChatList(
        userKey: String
    ) : Flow<List<ChatEntity>> {
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

    override suspend fun addChat(chatEntity: ChatEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            chatRemoteDataSource.add(chatEntity.toData())
            chatLocalDataSource.add(chatEntity.toData())
            cancel()
        }
    }

    override suspend fun modifyChat(
        chatEntity: ChatEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            chatRemoteDataSource.update(chatEntity.toData())
            chatLocalDataSource.update(chatEntity.toData())
            cancel()
        }
    }

    override suspend fun deleteChat(
        chatEntity: ChatEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            chatRemoteDataSource.remove(chatEntity.toData())
            chatLocalDataSource.remove(chatEntity.toData())
            cancel()
        }
    }

    private fun getEntities(data: List<ChatData>): List<ChatEntity> {
        val result = mutableListOf<ChatEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}