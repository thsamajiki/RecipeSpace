package com.hero.recipespace.view.main.chat

import com.google.firebase.Timestamp
import com.hero.recipespace.domain.message.entity.MessageEntity

data class MessageItem(
    val messageId: String = "",
    val chatKey: String,
    val userKey: String,
    val message: String?,
    val lastTimestamp: Timestamp?,
    val isRead: Boolean?,
    val userName: String = "",
    val displayOtherUserProfileImage: () -> String = {
        ""
    },
    val messageType: MessageType = MessageType.MESSAGE,
)

fun MessageEntity.toItem(displayOtherUserProfileImage: () -> String): MessageItem =
    MessageItem(
        messageId = messageId,
        chatKey = chatKey,
        userKey = userKey,
        message = message,
        lastTimestamp = timestamp,
        isRead = isRead,
        userName = userName,
        displayOtherUserProfileImage = displayOtherUserProfileImage,
        messageType = messageType,
    )
