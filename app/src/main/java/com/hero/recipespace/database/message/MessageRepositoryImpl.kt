package com.hero.recipespace.database.message

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.local.MessageLocalDataSource
import com.hero.recipespace.data.message.remote.MessageRemoteDataSource
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.mapper.toData
import com.hero.recipespace.domain.message.mapper.toEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override suspend fun getMessage(messageKey: String) : MessageEntity {
        return messageLocalDataSource.getData(messageKey).toEntity()
    }

    override fun getMessageList(userKey: String) : Flow<List<MessageEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            val messageList = messageRemoteDataSource.getDataList(userKey)
            messageLocalDataSource.addAll(messageList)
            cancel()
        }

        return messageLocalDataSource.observeDataList(userKey)
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addMessage(
        messageEntity: MessageEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            messageRemoteDataSource.add(messageEntity.toData())
            messageLocalDataSource.add(messageEntity.toData())
            cancel()
        }
    }

    override suspend fun modifyMessage(
        messageEntity: MessageEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            messageRemoteDataSource.update(messageEntity.toData())
            messageLocalDataSource.update(messageEntity.toData())
            cancel()
        }
    }

    override suspend fun deleteMessage(
        messageEntity: MessageEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            messageRemoteDataSource.remove(messageEntity.toData())
            messageLocalDataSource.remove(messageEntity.toData())
            cancel()
        }
    }

    private fun getEntities(data: List<MessageData>): List<MessageEntity> {
        val result = mutableListOf<MessageEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}