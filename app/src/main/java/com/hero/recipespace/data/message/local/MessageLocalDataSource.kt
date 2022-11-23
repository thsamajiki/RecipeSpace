package com.hero.recipespace.data.message.local

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.listener.OnCompleteListener

interface MessageLocalDataSource {
    fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>)

    fun getDataList(messageKey: String, onCompleteListener: OnCompleteListener<List<MessageData>>)

    fun clear()

    fun add(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    fun update(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)

    fun remove(messageData: MessageData, onCompleteListener: OnCompleteListener<MessageData>)
}