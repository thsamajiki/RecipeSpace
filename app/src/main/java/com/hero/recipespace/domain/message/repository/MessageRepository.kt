package com.hero.recipespace.domain.message.repository

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.listener.OnCompleteListener

interface MessageRepository {
    suspend fun getMessage(messageKey: String, onCompleteListener: OnCompleteListener<MessageEntity>) : LiveData<MessageEntity>

    fun getMessageList(userKey: String, onCompleteListener: OnCompleteListener<List<MessageEntity>>) : LiveData<List<MessageEntity>>

    suspend fun addMessage(messageEntity: MessageEntity, onCompleteListener: OnCompleteListener<MessageEntity>)

    suspend fun modifyMessage(messageEntity: MessageEntity, onCompleteListener: OnCompleteListener<MessageEntity>)

    suspend fun deleteMessage(messageEntity: MessageEntity, onCompleteListener: OnCompleteListener<MessageEntity>)
}