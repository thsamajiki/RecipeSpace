package com.hero.recipespace.domain.notice.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeEntity(
    var key: String,
    var title: String? = null,
    var desc: String? = null,
    var postDate: String? = null
) : Parcelable {

}