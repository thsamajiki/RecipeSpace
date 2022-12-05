package com.hero.recipespace.data.message.remote

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.datastore.MessageCloudStore
import com.hero.recipespace.listener.OnCompleteListener

class MessageRemoteDataSourceImpl(
    private val messageCloudStore: MessageCloudStore
) : MessageRemoteDataSource {

    override suspend fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>): LiveData<MessageData> {
        return messageCloudStore.getData(messageKey, onCompleteListener)
    }

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<MessageData>>
    ): LiveData<List<MessageData>> {
        return messageCloudStore.getDataList(userKey, onCompleteListener)
    }

    override suspend fun add(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>
    ) {
        messageCloudStore.add(messageData, onCompleteListener)
    }

    override suspend fun update(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>
    ) {
        messageCloudStore.update(messageData, onCompleteListener)
    }

    override suspend fun remove(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>
    ) {
        messageCloudStore.remove(messageData, onCompleteListener)
    }
}