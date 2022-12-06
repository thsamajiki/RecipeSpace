package com.hero.recipespace.data.chat.local

import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.datastore.ChatCacheStore
import com.hero.recipespace.database.chat.datastore.ChatLocalStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class ChatLocalDataSourceImpl(
    private val chatLocalStore: ChatLocalStore,
    private val chatCacheStore: ChatCacheStore
) : ChatLocalDataSource {

    override fun getData(chatKey: String) {
        chatCacheStore.getData(chatKey)



    }

    override fun getDataList(userKey: String) {
        chatCacheStore.getDataList()
    }

    override fun clear() {
        chatCacheStore.clear()
    }

    override suspend fun add(chatData: ChatData) {
        chatLocalStore.add(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    chatCacheStore.add(chatData, object : OnCompleteListener<ChatData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {

                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun update(chatData: ChatData) {

    }

    override suspend fun remove(chatData: ChatData) {
        chatLocalStore.remove(chatData)
    }
}