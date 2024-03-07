package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow

interface NoticeRemoteDataSource {
    suspend fun getData(noticeKey: String): NoticeData

    fun getDataList(): Flow<List<NoticeData>>

    suspend fun add(noticeData: NoticeData)

    suspend fun update(noticeData: NoticeData)

    suspend fun remove(noticeData: NoticeData)
}
