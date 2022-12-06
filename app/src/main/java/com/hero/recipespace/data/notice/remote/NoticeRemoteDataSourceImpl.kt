package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.database.notice.datastore.NoticeCloudStore
import kotlinx.coroutines.flow.Flow

class NoticeRemoteDataSourceImpl(
    private val noticeCloudStore: NoticeCloudStore
) : NoticeRemoteDataSource {
    override fun getData(noticeKey: String): Flow<NoticeData> {
        return noticeCloudStore.getData(noticeKey)
    }

    override fun getDataList(): Flow<List<NoticeData>> {
        return noticeCloudStore.getDataList()
    }

    override suspend fun add(noticeData: NoticeData) {
        noticeCloudStore.add(noticeData)
    }

    override suspend fun update(
        noticeData: NoticeData
    ) {
        noticeCloudStore.update(noticeData)
    }

    override suspend fun remove(
        noticeData: NoticeData
    ) {
        noticeCloudStore.remove(noticeData)
    }
}