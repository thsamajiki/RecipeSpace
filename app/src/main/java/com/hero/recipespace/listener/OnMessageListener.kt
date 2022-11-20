package com.hero.recipespace.listener

import com.hero.recipespace.data.message.MessageData

interface OnMessageListener {
    fun onMessage(isSuccess: Boolean, messageData: MessageData?)
}