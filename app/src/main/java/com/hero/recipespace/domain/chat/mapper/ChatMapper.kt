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

fun ChatEntity.toData(): ChatData {
    return ChatData(
        key = key,
        lastMessage = lastMessage,
        userProfiles = userProfiles,
        userNames = userNames,
        userList = userList
    )
}