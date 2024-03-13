package com.hero.recipespace.domain.recipe.request

data class UploadRecipeRequest(
    val content: String,
    val recipePhotoPathList: List<String>,
)
