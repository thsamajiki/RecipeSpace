package com.hero.recipespace.domain.user.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val key: String? = null,
    val name: String? = null,
    val email: String? = null,
    val profileImageUrl: String? = null
) : Parcelable {

}