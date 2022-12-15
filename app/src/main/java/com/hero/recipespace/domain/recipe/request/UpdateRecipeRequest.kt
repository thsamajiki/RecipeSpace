package com.hero.recipespace.domain.recipe.request

data class UpdateRecipeRequest(
    val content: String,
    val recipePhotoPathList: List<String>
)