package com.hero.recipespace.view.main.chat

import com.google.firebase.Timestamp
import com.hero.recipespace.domain.message.entity.MessageEntity

data class MessageItem(
    val chatKey: String,
    val userKey: String,
    val message: String?,
    val lastTimestamp: Timestamp?,
    val confirmed: Boolean?,
    val messageId: String = "",
    val userName: String = "",
    val displayOtherUserProfileImage: () -> String = {
        ""
    }
)

fun MessageEntity.toItem(
    displayOtherUserProfileImage: () -> String
): MessageItem  =
    MessageItem(
        chatKey = chatKey,
        userKey = userKey,
        message = message,
        lastTimestamp = timestamp,
        confirmed = confirmed,
        messageId = messageId,
        userName = userName,
        displayOtherUserProfileImage = displayOtherUserProfileImage
    )