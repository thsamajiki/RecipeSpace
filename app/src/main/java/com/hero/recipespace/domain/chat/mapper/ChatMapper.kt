package com.hero.recipespace.domain.chat.mapper

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.domain.chat.entity.ChatEntity

fun ChatData.toEntity(): ChatEntity {
    return ChatEntity(
        key = key,
        lastMessage = lastMessage,
        userProfiles = userProfiles,
        userNames = userNames,
        userList = userList
    )
}

fun ChatData.toData(chatEntity: ChatEntity): ChatData {
    return ChatData(
        key = chatEntity.key,
        lastMessage = chatEntity.lastMessage,
        userProfiles = chatEntity.userProfiles,
        userNames = chatEntity.userNames,
        userList = chatEntity.userList
    )
}