package com.hero.recipespace.data.message

import android.os.Parcelable
import androidx.room.Entity
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "message_db")
data class MessageData(
    val userKey: String,
    val timestamp: Timestamp
) : Parcelable {
}