package com.hero.recipespace.view.main.chat

import com.google.firebase.Timestamp
import com.hero.recipespace.domain.chat.entity.ChatEntity

data class ChatItem(
    val id: String,
    val unreadMessageCount: Int,
    val lastMessage: String,
    val lastTimestamp: Timestamp?,
    val displayOtherUserName: () -> String = {
        ""
    },
    val displayOtherUserProfileImage: () -> String = {
        ""
    }
)

fun ChatEntity.toItem(
    displayOtherUserName: () -> String,
    displayOtherUserProfileImage: () -> String
): ChatItem =
    ChatItem(
        id = key,
        unreadMessageCount = unreadMessageCount,
        lastMessage = lastMessage?.message.orEmpty(),
        lastTimestamp = lastMessage?.timestamp,
        displayOtherUserName = displayOtherUserName,
        displayOtherUserProfileImage = displayOtherUserProfileImage
    )