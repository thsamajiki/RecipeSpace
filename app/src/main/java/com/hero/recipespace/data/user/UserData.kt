package com.hero.recipespace.data.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val userKey: String? = null,
    val userName: String? = null,
    val profileImageUrl: String? = null
) : Parcelable {

}