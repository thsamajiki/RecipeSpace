package com.hero.recipespace.domain.rate.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateEntity(
    val key: String? = null,
    val userKey: String? = null,
    val userName: String? = null,
    val profileImageUrl: String? = null,
    val rate: Float = 0f,
    val date: Timestamp? = null
) : Parcelable {

}