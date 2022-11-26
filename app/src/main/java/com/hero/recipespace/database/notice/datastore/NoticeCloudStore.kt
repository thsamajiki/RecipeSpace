package com.hero.recipespace.database.notice.datastore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class NoticeCloudStore(
    private val context: Context
) : CloudStore<NoticeData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : NoticeCloudStore

        fun getInstance(context: Context) : NoticeCloudStore {
            return instance ?: synchronized(this) {
                instance ?: NoticeCloudStore(context).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<NoticeData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        onCompleteListener: OnCompleteListener<List<NoticeData>>
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>) {
        TODO("Not yet implemented")
    }
}