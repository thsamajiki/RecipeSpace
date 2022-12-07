package com.hero.recipespace.database.message

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.local.MessageLocalDataSource
import com.hero.recipespace.data.message.remote.MessageRemoteDataSource
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.mapper.toEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override fun getMessage(
        messageKey: String
    ) : Flow<MessageEntity> {
        return messageLocalDataSource.getData(messageKey)
            .map {
                it.toEntity()
            }
    }

    override fun getMessageList(
        userKey: String
    ) : Flow<List<MessageEntity>> {
        return messageLocalDataSource.getDataList(userKey)
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addMessage(
        messageEntity: MessageEntity
    ) {

    }

    override suspend fun modifyMessage(
        messageEntity: MessageEntity
    ) {

    }

    override suspend fun deleteMessage(
        messageEntity: MessageEntity
    ) {

    }

    private fun getEntities(data: List<MessageData>): List<MessageEntity> {
        val result = mutableListOf<MessageEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}