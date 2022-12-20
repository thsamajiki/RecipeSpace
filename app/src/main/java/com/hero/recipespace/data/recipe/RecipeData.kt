package com.hero.recipespace.data.recipe

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
@Entity(tableName = "recipe_db")
data class RecipeData(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    var key: String = "",
    @ColumnInfo(name = "profileImageUrl")
    var profileImageUrl: String? = null,
    @ColumnInfo(name = "userName")
    var userName: String? = null,
    @ColumnInfo(name = "userKey")
    var userKey: String = "",
    @ColumnInfo(name = "desc")
    var desc: String? = null,
    @ColumnInfo(name = "photoUrlList")
    var photoUrlList: List<String>? = null,
    @ColumnInfo(name = "postDate")
    var postDate: Timestamp? = null,
    @ColumnInfo(name = "rate")
    var rate: Float? = 0f,
    @ColumnInfo(name = "totalRatingCount")
    var totalRatingCount: Int? = 0
) : Parcelable {
//    constructor() : this(key, profileImageUrl, userName, userKey, desc, photoUrlList, postDate, rate, totalRatingCount)
}