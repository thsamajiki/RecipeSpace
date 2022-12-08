package com.hero.recipespace.domain.recipe.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeEntity(
    val key: String? = null,
    val profileImageUrl: String? = null,
    val userName: String? = null,
    val userKey: String? = null,
    val desc: String? = null,
    val photoUrlList: List<String>? = null,
    val postDate: Timestamp? = null,
    val rate: Float = 0f,
    val totalRatingCount: Int = 0
) : Parcelable {

}