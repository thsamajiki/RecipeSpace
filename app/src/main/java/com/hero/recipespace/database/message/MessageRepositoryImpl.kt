package com.hero.recipespace.database.message

import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.local.MessageLocalDataSource
import com.hero.recipespace.data.message.remote.MessageRemoteDataSource
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override fun getMessage(
        messageKey: String,
        onCompleteListener: OnCompleteListener<MessageEntity>
    ) {
        messageLocalDataSource.getData(messageKey, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageRemoteDataSource.getData(messageKey, object : OnCompleteListener<MessageData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<MessageData>?
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

    override fun getMessageList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<MessageEntity>>,
    ) {
        messageLocalDataSource.getDataList(userKey, object : OnCompleteListener<List<MessageData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<MessageData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageRemoteDataSource.getDataList(userKey, object : OnCompleteListener<List<MessageData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<MessageData>>?
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

    override fun addMessage(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        messageRemoteDataSource.add(messageData, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageLocalDataSource.add(messageData, object : OnCompleteListener<MessageData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<MessageData>?
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

    override fun modifyMessage(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        messageRemoteDataSource.update(messageData, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageLocalDataSource.update(messageData, object : OnCompleteListener<MessageData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<MessageData>?
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

    override fun deleteMessage(
        messageData: MessageData,
        onCompleteListener: OnCompleteListener<MessageEntity>,
    ) {
        messageRemoteDataSource.remove(messageData, object : OnCompleteListener<MessageData> {
            override fun onComplete(isSuccess: Boolean, response: Response<MessageData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    messageLocalDataSource.remove((messageData, object : OnCompleteListener<MessageData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<MessageData>?
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
}