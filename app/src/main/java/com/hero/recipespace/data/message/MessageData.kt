package com.hero.recipespace.data.message

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageData(
    val userKey: String,
    val timestamp: Timestamp
) : Parcelable {
}