package com.hero.recipespace.database.message

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.local.MessageLocalDataSource
import com.hero.recipespace.data.message.remote.MessageRemoteDataSource
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.mapper.toData
import com.hero.recipespace.domain.message.mapper.toEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnMessageListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override suspend fun getMessage(messageKey: String) : MessageEntity {
        return messageLocalDataSource.getData(messageKey).toEntity()
    }

    override fun getMessageList(chatKey: String) : Flow<List<MessageEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseData.getInstance()
                .getMessageList(chatKey, object : OnMessageListener {
                    override fun onMessage(isSuccess: Boolean, messageData: MessageData?) {
                        if (messageData != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                messageLocalDataSource.add(messageData)
                                cancel()
                            }
                        }
                    }
                })

            val messageList = messageRemoteDataSource.getDataList(chatKey)
            messageLocalDataSource.addAll(messageList)
            cancel()
        }

        return messageLocalDataSource.observeDataList(chatKey)
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addMessage(
        message: String
    ) {
        val result = messageRemoteDataSource.add(message)
        messageLocalDataSource.add(result)
    }

    override suspend fun modifyMessage(
        message: String
    ) {
        messageRemoteDataSource.update(messageEntity.toData())
        messageLocalDataSource.update(messageEntity.toData())
        cancel()
    }

    override suspend fun deleteMessage(
        message: String
    ) {
        messageRemoteDataSource.remove(messageEntity.toData())
        messageLocalDataSource.remove(messageEntity.toData())
        cancel()
    }

    private fun getEntities(data: List<MessageData>): List<MessageEntity> {
        val result = mutableListOf<MessageEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}