package com.hero.recipespace.domain.notice.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeEntity(
    val key: String? = null,
    val title: String? = null,
    val desc: String? = null,
    val postDate: String? = null
) : Parcelable {

}