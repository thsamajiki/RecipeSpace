package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.notice.service.NoticeService
import javax.inject.Inject

class NoticeRemoteDataSourceImpl @Inject constructor(
    private val noticeService: NoticeService
) : NoticeRemoteDataSource {

    override suspend fun getData(noticeKey: String): NoticeData {
        return noticeService.getData(noticeKey)
    }

    override suspend fun getDataList(): List<NoticeData> {
        return noticeService.getDataList()
    }

    override suspend fun add(noticeData: NoticeData) {
        noticeService.add(noticeData)
    }

    override suspend fun update(
        noticeData: NoticeData
    ) {
        noticeService.update(noticeData)
    }

    override suspend fun remove(
        noticeData: NoticeData
    ) {
        noticeService.remove(noticeData)
    }
}