package com.hero.recipespace.domain.message.repository

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.listener.OnCompleteListener

interface MessageRepository {
    suspend fun getMessage(messageKey: String, onCompleteListener: OnCompleteListener<MessageEntity>)

    fun getMessageList(userKey: String, onCompleteListener: OnCompleteListener<List<MessageEntity>>)

    suspend fun addMessage(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageEntity>)

    suspend fun modifyMessage(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageEntity>)

    suspend fun deleteMessage(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageEntity>)
}