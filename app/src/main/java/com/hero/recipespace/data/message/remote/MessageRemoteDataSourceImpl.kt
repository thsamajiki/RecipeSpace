package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.listener.OnCompleteListener

class MessageRemoteDataSourceImpl : MessageRemoteDataSource {
    override fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        messageKey: String,
        onCompleteListener: OnCompleteListener<List<MessageData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        TODO("Not yet implemented")
    }

    override fun update(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        TODO("Not yet implemented")
    }

    override fun remove(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        TODO("Not yet implemented")
    }
}