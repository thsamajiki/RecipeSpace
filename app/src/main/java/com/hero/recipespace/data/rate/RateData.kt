package com.hero.recipespace.data.rate

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "rate_db")
data class RateData(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    var key: String = "",
    @ColumnInfo(name = "userKey")
    var userKey: String = "",
    @ColumnInfo(name = "userName")
    var userName: String? = null,
    @ColumnInfo(name = "profileImageUrl")
    var profileImageUrl: String? = null,
    @ColumnInfo(name = "rate")
    var rate: Float = 0f,
    @ColumnInfo(name = "date")
    var date: Timestamp? = null
) : Parcelable {

}