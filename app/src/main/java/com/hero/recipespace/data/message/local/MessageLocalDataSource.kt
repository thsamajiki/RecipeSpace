package com.hero.recipespace.data.message.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.listener.OnCompleteListener

interface MessageLocalDataSource {
    suspend fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>) : LiveData<MessageData>

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<MessageData>>) : LiveData<List<MessageData>>

    fun clear()

    suspend fun add(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    suspend fun update(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    suspend fun remove(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)
}