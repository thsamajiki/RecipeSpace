package com.hero.recipespace.domain.chat.entity

import android.os.Parcelable
import com.hero.recipespace.domain.message.entity.MessageEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatEntity(
    val key: String = "",
    // @Embedded
    val lastMessage: MessageEntity? = null,
    val userProfileImages: Map<String, String>? = null,
    val userNames: Map<String, String>? = null,
    var userList: Map<String, Boolean>? = null
) : Parcelable {

    val displayOtherUserName: String
        get() = userNames?.toList()?.getOrNull(0)?.second.orEmpty()

}