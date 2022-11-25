package com.hero.recipespace.database.message.datastore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class MessageCloudStore(
    private val context: Context
) : CloudStore<MessageData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : MessageCloudStore

        fun getInstance(context: Context) : MessageCloudStore {
            return instance ?: synchronized(this) {
                instance ?: MessageCloudStore(context).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<MessageData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
        TODO("Not yet implemented")
    }
}