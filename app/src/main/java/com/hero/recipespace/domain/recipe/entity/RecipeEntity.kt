package com.hero.recipespace.domain.recipe.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeEntity(
    var key: String = "",
    var profileImageUrl: String? = null,
    var userName: String = "",
    var userKey: String = "",
    var desc: String? = null,
    var photoUrlList: List<String>? = null,
    var postDate: Timestamp? = null,
    var rate: Float? = 0f,
    var totalRatingCount: Int? = 0,
) : Parcelable {
    val thumbnailPhoto: String?
        get() = photoUrlList?.firstOrNull()
}
