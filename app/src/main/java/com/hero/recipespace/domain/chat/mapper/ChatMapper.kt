package com.hero.recipespace.domain.chat.mapper

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.message.mapper.toData
import com.hero.recipespace.domain.message.mapper.toEntity

fun ChatData.toEntity(): ChatEntity {
    return ChatEntity(
        key = key,
        lastMessage = lastMessage?.toEntity(),
        userProfileImages = userProfileImages,
        userNames = userNames,
        userList = userList,
        recipeKey = recipeKey,
        unreadMessageCount = unreadMessageCount,
    )
}

fun ChatEntity.toData(): ChatData {
    return ChatData(
        key = key,
        lastMessage = lastMessage?.toData(),
        userProfileImages = userProfileImages,
        userNames = userNames,
        userList = userList,
        recipeKey = recipeKey,
        unreadMessageCount = unreadMessageCount,
    )
}
