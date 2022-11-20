package com.hero.recipespace.data.chat

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.hero.recipespace.data.message.MessageData
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatData(
    @PrimaryKey
    val key: String,
    @ColumnInfo(name = "lastMessage")
    val lastMessage: MessageData? = null,
    @ColumnInfo(name = "userProfiles")
    val userProfiles: HashMap<String, String>? = null,
    @ColumnInfo(name = "userNames")
    val userNames: HashMap<String, String>? = null,
    @ColumnInfo(name = "userList")
    val userList: HashMap<String, Boolean>? = null
) : Parcelable {

}