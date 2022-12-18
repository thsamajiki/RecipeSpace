package com.hero.recipespace.domain.notice.repository

import com.hero.recipespace.domain.notice.entity.NoticeEntity

interface NoticeRepository {
    suspend fun getNotice(noticeKey: String) : NoticeEntity

    suspend fun getNoticeList() : List<NoticeEntity>
}