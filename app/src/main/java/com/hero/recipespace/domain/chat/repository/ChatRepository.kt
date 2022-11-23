package com.hero.recipespace.domain.chat.repository

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.listener.OnCompleteListener

interface ChatRepository {
    fun getChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun getChatList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatEntity>>)

    fun addChat(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun modifyChat(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun deleteChat(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatEntity>)
}