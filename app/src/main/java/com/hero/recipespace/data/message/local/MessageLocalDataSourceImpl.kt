package com.hero.recipespace.data.message.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.datastore.MessageCacheStore
import com.hero.recipespace.database.message.datastore.MessageLocalStore
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import kotlinx.coroutines.flow.Flow

class MessageLocalDataSourceImpl(
    private val messageLocalStore: MessageLocalStore,
    private val messageCacheStore: MessageCacheStore
) : MessageLocalDataSource {

    override fun getData(messageKey: String) : Flow<MessageData> {

    }

    override fun getDataList(
        userKey: String
    ): Flow<List<MessageData>> {

    }

    override fun clear() {
        messageCacheStore.clear()
    }

    override suspend fun add(
        messageData: MessageData
    ) {
        messageLocalStore.add(messageData, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    messageCacheStore.add(messageData, object : OnCompleteListener<MessageData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<MessageData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun update(
        messageData: MessageData
    ) {

    }

    override suspend fun remove(
        messageData: MessageData
    ) {

    }
}