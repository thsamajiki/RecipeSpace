package com.hero.recipespace.database.chat

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.local.ChatLocalDataSource
import com.hero.recipespace.data.chat.remote.ChatRemoteDataSource
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.mapper.toData
import com.hero.recipespace.domain.chat.mapper.toEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource
) : ChatRepository {

    override fun getChat(chatKey: String) : Flow<ChatEntity> {
        return chatRemoteDataSource.getData(chatKey)
            .map {
                it.toEntity()
            }
    }

    override fun getChatList(
        userKey: String
    ) : Flow<List<ChatEntity>> {
        return chatRemoteDataSource.getDataList(userKey)
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addChat(chatEntity: ChatEntity) {

    }

    override suspend fun modifyChat(
        chatEntity: ChatEntity
    ) {
        chatRemoteDataSource.update(ChatData.toData(chatEntity))
    }

    override suspend fun deleteChat(
        chatEntity: ChatEntity
    ) {

    }

    private fun getEntities(data: List<ChatData>): List<ChatEntity> {
        val result = mutableListOf<ChatEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}