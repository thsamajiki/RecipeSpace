package com.hero.recipespace.data.notice.service

import com.hero.recipespace.data.notice.NoticeData

interface NoticeService {
    suspend fun getData(noticeKey: String) : NoticeData

    suspend fun getDataList() : List<NoticeData>

    suspend fun add(noticeData: NoticeData)

    suspend fun update(noticeData: NoticeData)

    suspend fun remove(noticeData: NoticeData)
}