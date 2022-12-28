package com.hero.recipespace.view.main.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OtherUserInfo(
    val key: String,
    val name: String
): Parcelable