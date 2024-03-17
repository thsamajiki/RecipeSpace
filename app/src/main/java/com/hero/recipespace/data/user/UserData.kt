package com.hero.recipespace.data.user

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "user_db")
data class UserData(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    var key: String = "",
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "email")
    var email: String? = null,
    @ColumnInfo(name = "profileImageUrl")
    var profileImageUrl: String? = null,
) : Parcelable
