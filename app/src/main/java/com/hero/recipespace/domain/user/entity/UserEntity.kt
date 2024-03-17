package com.hero.recipespace.domain.user.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    @PrimaryKey
    @NonNull
    var key: String = "",
    var name: String? = null,
    var email: String? = null,
    var profileImageUrl: String? = null,
) : Parcelable
