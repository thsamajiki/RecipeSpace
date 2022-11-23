package com.hero.recipespace.database.chat

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.listener.OnCompleteListener

class ChatRepositoryImpl : ChatRepository {
    override fun getChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) {
        TODO("Not yet implemented")
    }

    override fun getChatList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<ChatEntity>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun addChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) {
        TODO("Not yet implemented")
    }

    override fun modifyChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) {
        TODO("Not yet implemented")
    }

    override fun deleteChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) {
        TODO("Not yet implemented")
    }
}