package com.hero.recipespace.database.chat

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.local.ChatLocalDataSource
import com.hero.recipespace.data.chat.remote.ChatRemoteDataSource
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource
) : ChatRepository {
    override fun getChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) {
        TODO("Not yet implemented")
    }

    override fun getChatList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<ChatEntity>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun addChat(chatEntity: ChatEntity, onCompleteListener: OnCompleteListener<ChatEntity>) {
        chatRemoteDataSource.add(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun modifyChat(
        chatEntity: ChatEntity,
        onCompleteListener: OnCompleteListener<ChatEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteChat(
        chatEntity: ChatEntity,
        onCompleteListener: OnCompleteListener<ChatEntity>,
    ) {
        TODO("Not yet implemented")
    }
}