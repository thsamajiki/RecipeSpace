package com.hero.recipespace.domain.user.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val userKey: String? = null,
    val userName: String? = null,
    val profileImageUrl: String? = null
) : Parcelable {

}