package com.hero.recipespace.data.message.remote

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.listener.OnCompleteListener

interface MessageRemoteDataSource {
    suspend fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>) : LiveData<MessageData>

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<MessageData>>) : LiveData<List<MessageData>>

    suspend fun add(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    suspend fun update(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    suspend fun remove(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)
}