package com.hero.recipespace.domain.message.request

import com.hero.recipespace.domain.message.entity.MessageEntity

data class ReadMessageRequest(
    val chatKey: String,
    val unreadMessageList: List<MessageEntity>,
    val userKey: String
)