package com.hero.recipespace.domain.message.mapper

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.domain.message.entity.MessageEntity

fun MessageData.toEntity(): MessageEntity {
    return MessageEntity(
        userKey = userKey,
        timestamp = timestamp
    )
}

fun MessageEntity.toData(): MessageData {
    return MessageData(
        userKey = userKey,
        timestamp = timestamp
    )
}