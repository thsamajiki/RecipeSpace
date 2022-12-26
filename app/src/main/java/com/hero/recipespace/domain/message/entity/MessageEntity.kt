package com.hero.recipespace.domain.message.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageEntity(
    var userKey: String = "",
    var message: String? = null,
    var timestamp: Timestamp? = null,
    var confirmed: Boolean? = null,
    @NonNull
    @PrimaryKey
    var messageId: String = ""
) : Parcelable {

}