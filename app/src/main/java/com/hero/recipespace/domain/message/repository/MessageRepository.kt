package com.hero.recipespace.domain.message.repository

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.listener.OnCompleteListener

interface MessageRepository {
    fun getMessage(messageKey: String, onCompleteListener: OnCompleteListener<MessageEntity>)

    fun getMessageList(userKey: String, onCompleteListener: OnCompleteListener<List<MessageEntity>>)

    fun addMessage(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageEntity>)

    fun modifyMessage(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageEntity>)

    fun deleteMessage(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageEntity>)
}