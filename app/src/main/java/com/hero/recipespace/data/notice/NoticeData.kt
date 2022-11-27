package com.hero.recipespace.data.notice

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeData(
    val key: String? = null,
    val noticeTitle: String? = null,
    val noticeDesc: String? = null,
    val noticeDate: String? = null
) : Parcelable {

}