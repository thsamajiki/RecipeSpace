package com.hero.recipespace.database.chat.datastore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class ChatCloudStore(
    private val context: Context,
    private val chatLocalStore: ChatLocalStore,
    private val chatCacheStore: ChatCacheStore
) : CloudStore<ChatData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : ChatCloudStore

        fun getInstance(context: Context) : ChatCloudStore {
            return instance ?: synchronized(this) {
                instance ?: ChatCloudStore(context, chatLocalStore, chatCacheStore).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<ChatData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

}