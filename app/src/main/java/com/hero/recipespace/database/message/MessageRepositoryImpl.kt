package com.hero.recipespace.database.message

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.local.MessageLocalDataSource
import com.hero.recipespace.data.message.remote.MessageRemoteDataSource
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.mapper.toEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val chatRepository: ChatRepository
) : MessageRepository {

    override suspend fun getMessage(messageKey: String) : MessageEntity {
        return messageLocalDataSource.getData(messageKey).toEntity()
    }

    override fun getMessageList(chatKey: String) : Flow<List<MessageEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            messageRemoteDataSource.getDataList(chatKey)
                .collect { messageList ->
                    if (messageList.isNotEmpty()) {
                        messageLocalDataSource.addAll(messageList)
                    }
                }
        }

        return messageLocalDataSource.observeDataList(chatKey)
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addMessage(
        chatKey: String,
        otherUserKey: String,
        message: String
    ) {
        if (chatKey.isNotEmpty()) { // 기존 채팅방이 있음.
            val result = messageRemoteDataSource.add(chatKey, message)
            messageLocalDataSource.add(result)
        } else if (otherUserKey.isNotEmpty()) {
            chatRepository.createNewChatRoom(otherUserKey, message)
        }
    }

    override suspend fun modifyMessage(
        chatKey: String,
        message: String
    ) {
        val result = messageRemoteDataSource.update(chatKey, message)
        messageLocalDataSource.update(result)
    }

    override suspend fun deleteMessage(
        chatKey: String,
        message: String
    ) {
        val result = messageRemoteDataSource.remove(chatKey, message)
        messageLocalDataSource.remove(result)
    }

    private fun getEntities(data: List<MessageData>): List<MessageEntity> {
        val result = mutableListOf<MessageEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}