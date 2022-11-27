package com.hero.recipespace.domain.notice.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeEntity(
    val key: String? = null,
    val noticeTitle: String? = null,
    val noticeDesc: String? = null,
    val noticeDate: String? = null
) : Parcelable {

}