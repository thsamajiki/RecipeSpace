package com.hero.recipespace.domain.message.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageEntity(
    var userKey: String = "",
    var message: String? = null,
    var timestamp: Timestamp? = null,
    var confirmed: Boolean? = null,
    var messageId: String = "",
    var userName: String = ""
) : Parcelable {

}