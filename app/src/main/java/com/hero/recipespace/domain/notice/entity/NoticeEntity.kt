package com.hero.recipespace.domain.notice.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeEntity(
    var key: String = "",
    var title: String? = null,
    var desc: String? = null,
    var postDate: Timestamp? = null,
) : Parcelable
