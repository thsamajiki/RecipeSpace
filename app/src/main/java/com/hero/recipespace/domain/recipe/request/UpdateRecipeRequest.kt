package com.hero.recipespace.domain.recipe.request

data class UpdateRecipeRequest(
    val key: String,
    val content: String,
    val recipePhotoPathList: List<String>,
)
