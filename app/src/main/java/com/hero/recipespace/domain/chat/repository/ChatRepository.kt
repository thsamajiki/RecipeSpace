package com.hero.recipespace.domain.chat.repository

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.listener.OnCompleteListener

interface ChatRepository {
    fun getChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun getChatList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatEntity>>)

    fun addChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun modifyChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun deleteChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>)
}