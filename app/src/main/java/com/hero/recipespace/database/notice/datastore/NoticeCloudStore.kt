package com.hero.recipespace.database.notice.datastore

import com.hero.recipespace.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow

class NoticeCloudStore() {

    companion object {
        private lateinit var instance : NoticeCloudStore

        fun getInstance() : NoticeCloudStore {
            return synchronized(this) {
                instance ?: NoticeCloudStore().also {
                    instance = it
                }
            }
        }
    }

    fun getData(noticeKey: String) : Flow<NoticeData> {
        TODO("Not yet implemented")
    }

    fun getDataList(): Flow<List<NoticeData>> {
        TODO("Not yet implemented")
    }

    fun add(data: NoticeData) {
        TODO("Not yet implemented")
    }

    fun update(data: NoticeData) {
        TODO("Not yet implemented")
    }

    fun remove(data: NoticeData) {
        TODO("Not yet implemented")
    }
}