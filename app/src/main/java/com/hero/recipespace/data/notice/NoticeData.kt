package com.hero.recipespace.data.notice

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notice_db")
data class NoticeData(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "key")
    var key: String,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "desc")
    var desc: String? = null,
    @ColumnInfo(name = "postDate")
    var postDate: String? = null
)