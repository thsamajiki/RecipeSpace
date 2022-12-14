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
    var chatKey: String = "",
    var userKey: String = "",
    var message: String? = null,
    var timestamp: Timestamp? = null,
    var confirmed: Boolean? = null,
    @PrimaryKey
    var messageId: String = ""
) : Parcelable {
}