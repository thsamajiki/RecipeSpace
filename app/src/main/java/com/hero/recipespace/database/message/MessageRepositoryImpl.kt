package com.hero.recipespace.database.message

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener

class MessageRepositoryImpl : MessageRepository {
    override fun getMessage(
        messageKey: String,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun getMessageList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<MessageEntity>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun addMessage(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun modifyMessage(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        TODO("Not yet implemented")
    }

}