package com.hero.recipespace.data.rate

import android.os.Parcelable
import androidx.room.Entity
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "rate_db")
data class RateData(
    val key: String? = null,
    val userKey: String? = null,
    val userName: String? = null,
    val profileImageUrl: String? = null,
    val rate: Float = 0f,
    val date: Timestamp? = null
) : Parcelable {

}