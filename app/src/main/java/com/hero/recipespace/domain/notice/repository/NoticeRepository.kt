package com.hero.recipespace.domain.notice.repository

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    suspend fun getNotice(noticeKey: String): NoticeEntity

    fun getNoticeList(): Flow<List<NoticeEntity>>
}
