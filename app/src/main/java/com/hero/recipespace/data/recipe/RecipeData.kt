package com.hero.recipespace.data.recipe

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeData(
    val key: String? = null,
    val profileImageUrl: String? = null,
    val userName: String? = null,
    val userKey: String? = null,
    val desc: String? = null,
    val photoUrl: String? = null,
    val postDate: Timestamp? = null,
    val rate: Float = 0f,
    val totalRatingCount: Int = 0
) : Parcelable {

}