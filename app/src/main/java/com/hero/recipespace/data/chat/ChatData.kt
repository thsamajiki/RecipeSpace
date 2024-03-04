package com.hero.recipespace.data.chat

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hero.recipespace.data.message.MessageData

@Keep
@Entity(tableName = "chat_db")
data class ChatData(
    @PrimaryKey
    @ColumnInfo(name = "key")
    var key: String = "",
    @ColumnInfo(name = "lastMessage")
    var lastMessage: MessageData? = null,
    @ColumnInfo(name = "userProfiles")
    var userProfileImages: Map<String, String>? = null,
    @ColumnInfo(name = "userNames")
    var userNames: Map<String, String>? = null,
    @ColumnInfo(name = "userList")
    var userList: List<String>? = null,
    @ColumnInfo(name = "recipeKey")
    var recipeKey: String = "",
    @ColumnInfo(name = "unreadMessageCount")
    var unreadMessageCount: Int = 0,
)
