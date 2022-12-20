package com.hero.recipespace.data.message

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "message_db")
data class MessageData(
    @NonNull
    @PrimaryKey
    var userKey: String,
    var message: String? = null,
    var timestamp: Timestamp? = null,
    var confirmed: Boolean? = null
) : Parcelable {
}