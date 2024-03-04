package com.hero.recipespace.domain.chat.request

import com.hero.recipespace.domain.message.entity.MessageEntity

data class CountUnreadMessageRequest(
    val chatKey: String,
    val unreadMessageList: List<MessageEntity>,
    val userKey: String,
    val count: Int,
)
