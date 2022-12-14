package com.hero.recipespace.data.notice

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notice_db")
data class NoticeData(
    val key: String? = null,
    val title: String? = null,
    val desc: String? = null,
    val postDate: String? = null
) : Parcelable {

}