package com.hero.recipespace.data.notice.remote

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.database.notice.datastore.NoticeCloudStore
import com.hero.recipespace.listener.OnCompleteListener

class NoticeRemoteDataSourceImpl(
    private val noticeCloudStore: NoticeCloudStore
) : NoticeRemoteDataSource {
    override suspend fun getData(noticeKey: String, onCompleteListener: OnCompleteListener<NoticeData>): LiveData<NoticeData> {
        return noticeCloudStore.getData(noticeKey, onCompleteListener)
    }

    override fun getDataList(
        onCompleteListener: OnCompleteListener<List<NoticeData>>
    ): LiveData<List<NoticeData>> {
        return noticeCloudStore.getDataList(onCompleteListener)
    }

    override suspend fun add(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>) {
        noticeCloudStore.add(noticeData, onCompleteListener)
    }

    override suspend fun update(
        noticeData: NoticeData,
        onCompleteListener: OnCompleteListener<NoticeData>
    ) {
        noticeCloudStore.update(noticeData, onCompleteListener)
    }

    override suspend fun remove(
        noticeData: NoticeData,
        onCompleteListener: OnCompleteListener<NoticeData>
    ) {
        noticeCloudStore.remove(noticeData, onCompleteListener)
    }
}