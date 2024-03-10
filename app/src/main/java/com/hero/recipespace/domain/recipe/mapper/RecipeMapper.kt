package com.hero.recipespace.domain.recipe.mapper

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.recipe.entity.RecipeEntity

fun RecipeData.toEntity(): RecipeEntity {
    return RecipeEntity(
        key = key,
        profileImageUrl = profileImageUrl,
        userName = userName,
        userKey = userKey,
        desc = desc,
        photoUrlList = photoUrlList,
        postDate = postDate,
        rate = rate,
        totalRatingCount = totalRatingCount,
    )
}

fun RecipeEntity.toData(): RecipeData {
    return RecipeData(
        key = key,
        profileImageUrl = profileImageUrl,
        userName = userName,
        userKey = userKey,
        desc = desc,
        photoUrlList = photoUrlList,
        postDate = postDate,
        rate = rate,
        totalRatingCount = totalRatingCount,
    )
}
