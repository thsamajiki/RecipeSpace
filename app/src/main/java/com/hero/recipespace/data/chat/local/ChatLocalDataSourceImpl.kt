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

    override suspend fun getData(chatKey: String, onCompleteListener: OnCompleteListener<ChatData>) {
        chatCacheStore.getData(chatKey, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    chatLocalStore.getData(chatKey, object : OnCompleteListener<ChatData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<ChatData>>) {
        chatCacheStore.getDataList(object : OnCompleteListener<List<ChatData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<ChatData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    chatLocalStore.getDataList(object : OnCompleteListener<List<ChatData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<ChatData>>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun clear() {
        chatCacheStore.clear()
    }

    override suspend fun add(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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

    override suspend fun update(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatLocalStore.update(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    chatCacheStore.update(chatData, object : OnCompleteListener<ChatData> {
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

    override suspend fun remove(chatData: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        chatLocalStore.remove(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    chatCacheStore.remove(chatData, object : OnCompleteListener<ChatData> {
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
}