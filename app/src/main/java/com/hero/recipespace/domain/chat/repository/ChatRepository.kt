package com.hero.recipespace.domain.chat.repository

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.listener.OnCompleteListener

interface ChatRepository {
    suspend fun getChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>)

    fun getChatList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatEntity>>)

    suspend fun addChat(chatEntity: ChatEntity, onCompleteListener: OnCompleteListener<ChatEntity>)

    suspend fun modifyChat(chatEntity: ChatEntity, onCompleteListener: OnCompleteListener<ChatEntity>)

    suspend fun deleteChat(chatEntity: ChatEntity, onCompleteListener: OnCompleteListener<ChatEntity>)
}