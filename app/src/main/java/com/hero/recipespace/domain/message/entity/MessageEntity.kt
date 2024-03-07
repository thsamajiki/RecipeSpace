package com.hero.recipespace.domain.message.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.hero.recipespace.view.main.chat.MessageType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageEntity(
    var messageId: String = "",
    var chatKey: String = "",
    var userKey: String = "",
    var message: String? = null,
    var timestamp: Timestamp? = null,
    var isRead: Boolean? = null,
    var userName: String = "",
    var messageType: MessageType,
) : Parcelable
