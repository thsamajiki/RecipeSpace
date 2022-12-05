package com.hero.recipespace.database.chat

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.local.ChatLocalDataSource
import com.hero.recipespace.data.chat.remote.ChatRemoteDataSource
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource
) : ChatRepository {

    override suspend fun getChat(chatKey: String, onCompleteListener: OnCompleteListener<ChatEntity>) : LiveData<ChatEntity> {
        chatLocalDataSource.getData(chatKey, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {

                } else {
                    chatRemoteDataSource.getData(chatKey, object : OnCompleteListener<ChatData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                            if (isSuccess) {

                            } else {
                                Toast.makeText(this, "채팅방을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        })
    }

    override fun getChatList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<ChatEntity>>,
    ) : LiveData<List<ChatEntity>> {
        chatLocalDataSource.getDataList(userKey, object : OnCompleteListener<List<ChatData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<ChatData>>?) {
                if (isSuccess) {

                } else {
                    chatRemoteDataSource.getDataList(userKey, object : OnCompleteListener<List<ChatData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<ChatData>>?
                        ) {
                            if (isSuccess) {

                            } else {
                                Toast.makeText(this, "채팅방 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        })
    }

    override suspend fun addChat(chatEntity: ChatEntity, onCompleteListener: OnCompleteListener<ChatEntity>) {
        chatRemoteDataSource.add(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    chatLocalDataSource.add(chatData, object : OnCompleteListener<ChatData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                            if (isSuccess) {

                            } else {

                            }
                        }
                    })
                } else {
                    Toast.makeText(this, "채팅방 생성을 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override suspend fun modifyChat(
        chatEntity: ChatEntity,
        onCompleteListener: OnCompleteListener<ChatEntity>,
    ) {
        chatRemoteDataSource.update(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    chatLocalDataSource.update(chatData, object : OnCompleteListener<ChatData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                            if (isSuccess) {

                            } else {

                            }
                        }
                    })
                } else {
                    Toast.makeText(this, "채팅방 변경을 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override suspend fun deleteChat(
        chatEntity: ChatEntity,
        onCompleteListener: OnCompleteListener<ChatEntity>,
    ) {
        chatRemoteDataSource.remove(chatData, object : OnCompleteListener<ChatData> {
            override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                if (isSuccess) {
                    chatLocalDataSource.remove(chatData, object : OnCompleteListener<ChatData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<ChatData>?) {
                            if (isSuccess) {

                            } else {

                            }
                        }
                    })
                } else {
                    Toast.makeText(this, "채팅방 삭제를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}