package com.hero.recipespace.view.main.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeChatInfo(
    val userKey: String,
    val userName: String,
    val userProfileImageUrl: String?,
    val recipeKey: String,
) : Parcelable