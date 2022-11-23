package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.listener.OnCompleteListener

interface MessageRemoteDataSource {
    fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>)

    fun getDataList(messageKey: String, onCompleteListener: OnCompleteListener<List<MessageData>>)

    fun add(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    fun update(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    fun remove(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)
}