package com.hero.recipespace.data.chat.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.listener.OnCompleteListener

interface ChatLocalDataSource {
    suspend fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>) : LiveData<ChatData>

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatData>>) : LiveData<List<ChatData>>

    fun clear()

    suspend fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    suspend fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)

    suspend fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>)
}