package com.hero.recipespace.data.message

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "message_db")
data class MessageData(
    @PrimaryKey
    var messageId: String = "",
    var chatKey: String = "",
    var userKey: String = "",
    var message: String? = null,
    var timestamp: Timestamp? = null,
    var isRead: Boolean? = false
) : Parcelable {
}