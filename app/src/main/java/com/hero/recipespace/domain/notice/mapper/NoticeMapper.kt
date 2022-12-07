package com.hero.recipespace.domain.notice.mapper

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.domain.notice.entity.NoticeEntity

fun NoticeData.toEntity(): NoticeEntity {
    return NoticeEntity(
        key = key,
        noticeTitle = noticeTitle,
        noticeDesc = noticeDesc,
        noticeDate = noticeDate
    )
}

fun NoticeEntity.toData(): NoticeData {
    return NoticeData(
        key = key,
        noticeTitle = noticeTitle,
        noticeDesc = noticeDesc,
        noticeDate = noticeDate
    )
}