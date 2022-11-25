package com.hero.recipespace.data.message.remote

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.datastore.MessageCloudStore
import com.hero.recipespace.listener.OnCompleteListener

class MessageRemoteDataSourceImpl(
    private val messageCloudStore: MessageCloudStore
) : MessageRemoteDataSource {

    override fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>) {
        messageCloudStore.getData(messageKey, onCompleteListener)
    }

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<MessageData>>
    ) {
        messageCloudStore.getDataList(userKey, onCompleteListener)
    }

    override fun add(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        messageCloudStore.add(messageData, onCompleteListener)
    }

    override fun update(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        messageCloudStore.update(messageData, onCompleteListener)
    }

    override fun remove(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        messageCloudStore.remove(messageData, onCompleteListener)
    }
}