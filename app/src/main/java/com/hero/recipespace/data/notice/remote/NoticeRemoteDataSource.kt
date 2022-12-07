package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData

interface NoticeRemoteDataSource {
    suspend fun getData(noticeKey: String) : NoticeData

    suspend fun getDataList() : List<NoticeData>

    suspend fun add(noticeData: NoticeData)

    suspend fun update(noticeData: NoticeData)

    suspend fun remove(noticeData: NoticeData)
}