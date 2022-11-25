package com.hero.recipespace.data.message.local

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.datastore.MessageCacheStore
import com.hero.recipespace.database.message.datastore.MessageLocalStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class MessageLocalDataSourceImpl(
    private val messageLocalStore: MessageLocalStore,
    private val messageCacheStore: MessageCacheStore
) : MessageLocalDataSource {

    override fun getData(messageKey: String, onCompleteListener: OnCompleteListener<MessageData>) {
        messageCacheStore.getData(messageKey, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageLocalStore.getData(messageKey, object : OnCompleteListener<MessageData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?
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

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<MessageData>>,
    ) {
        messageCacheStore.getDataList(object : OnCompleteListener<List<MessageData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<MessageData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageLocalStore.getDataList(object : OnCompleteListener<List<MessageData>> {
                        override fun onComplete(isSuccess: Boolean, response: Response<List<MessageData>>?
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
        messageCacheStore.clear()
    }

    override fun add(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
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

    override fun update(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        messageLocalStore.update(messageData, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    messageCacheStore.update(messageData, object : OnCompleteListener<MessageData> {
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

    override fun remove(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageData>,
    ) {
        messageLocalStore.remove(messageData, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    messageCacheStore.remove(messageData, object : OnCompleteListener<MessageData> {
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
}