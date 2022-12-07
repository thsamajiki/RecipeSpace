package com.hero.recipespace.data.notice.service

import com.hero.recipespace.data.notice.NoticeData
import javax.inject.Inject

class NoticeServiceImpl @Inject constructor() : NoticeService {
    override suspend fun getData(noticeKey: String): NoticeData {
        TODO("Not yet implemented")
    }

    override suspend fun getDataList(): List<NoticeData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(noticeData: NoticeData) {
        TODO("Not yet implemented")
    }

    override suspend fun update(noticeData: NoticeData) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(noticeData: NoticeData) {
        TODO("Not yet implemented")
    }
}