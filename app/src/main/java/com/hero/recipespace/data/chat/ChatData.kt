package com.hero.recipespace.data.chat

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hero.recipespace.data.message.MessageData

@Entity(tableName = "chat_db")
data class ChatData(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    var key: String,
    @ColumnInfo(name = "lastMessage") // @Embedded
    var lastMessage: MessageData? = null,
    @ColumnInfo(name = "userProfiles")
    var userProfileImages: Map<String, String>? = null,
    @ColumnInfo(name = "userNames")
    var userNames: Map<String, String>? = null,
    @ColumnInfo(name = "userList")
    var userList: Map<String, Boolean>? = null
)