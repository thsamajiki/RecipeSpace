package com.hero.recipespace.data.user

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_db")
data class UserData(
    val userKey: String? = null,
    var userName: String? = null,
    val profileImageUrl: String? = null
) : Parcelable {

}