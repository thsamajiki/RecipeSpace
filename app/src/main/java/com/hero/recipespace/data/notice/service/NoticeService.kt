package com.hero.recipespace.data.notice.service

import com.hero.recipespace.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow

interface NoticeService {
    suspend fun getData(noticeKey: String) : NoticeData

    fun getDataList() : Flow<List<NoticeData>>

    suspend fun add(noticeData: NoticeData)

    suspend fun update(noticeData: NoticeData)

    suspend fun remove(noticeData: NoticeData)
}