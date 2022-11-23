package com.hero.recipespace.domain.message.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageEntity(
    val userKey: String,
    val timestamp: Timestamp
) : Parcelable {
}