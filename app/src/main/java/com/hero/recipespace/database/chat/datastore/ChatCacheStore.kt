package com.hero.recipespace.database.chat.datastore

import android.content.Context
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.CacheStore
import com.hero.recipespace.listener.OnCompleteListener

class ChatCacheStore: CacheStore<ChatData>() {

    companion object {
        private lateinit var instance : ChatCacheStore

        fun getInstance(context: Context) : ChatCacheStore {
            return instance ?: synchronized(this) {
                instance ?: ChatCacheStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<ChatData>) {
        val chatKey: String = params[0].toString()

        for (chatData in getDataList()) {
            if (chatData.key == chatKey) {
                onCompleteListener.onComplete(true, chatData)
                return
            }
        }
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<ChatData>>,
    ) {
        if (getDataList().isEmpty()) {
            onCompleteListener.onComplete(true, null)
        } else {
            onCompleteListener.onComplete(false, getDataList())
        }
    }

    override fun add(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        getDataList().add(data)

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data)
        }
    }

    override fun update(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        val originIndex = getDataList().indexOf(data)
        if (originIndex == -1) {
            throw IndexOutOfBoundsException("기존 데이터가 없습니다.")
        } else {
            getDataList().set(originIndex, data)
        }

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data)
        }
    }

    override fun remove(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        val originIndex = getDataList().indexOf(data)
        if (originIndex == -1) {
            throw IndexOutOfBoundsException("기존 데이터가 없습니다.")
        } else {
            getDataList().remove(originIndex)
        }
    }
}