package com.hero.recipespace.data.message.request

import com.hero.recipespace.data.message.MessageData

data class ReadMessageDataRequest(
    val chatKey: String,
    val unreadMessageList: List<MessageData>,
    val userKey: String,
)
