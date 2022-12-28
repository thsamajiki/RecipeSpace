package com.hero.recipespace.domain.message.mapper

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.util.WLog

fun MessageData.toEntity(): MessageEntity {
    WLog.d("messageId $messageId")

    return MessageEntity(
        chatKey = chatKey,
        userKey = userKey,
        message = message,
        timestamp = timestamp,
        confirmed = confirmed,
        messageId = messageId
    )
}

fun MessageEntity.toData(): MessageData {
    return MessageData(
        chatKey = chatKey,
        userKey = userKey,
        message = message,
        timestamp = timestamp,
        confirmed = confirmed,
        messageId = messageId
    )
}