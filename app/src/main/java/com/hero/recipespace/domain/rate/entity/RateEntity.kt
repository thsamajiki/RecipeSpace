package com.hero.recipespace.domain.rate.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateEntity(
    var rateKey: String = "",
    var rate: Float = 0f,
    var date: Timestamp? = null
) : Parcelable {

}