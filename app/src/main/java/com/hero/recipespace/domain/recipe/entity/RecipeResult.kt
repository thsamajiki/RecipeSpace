package com.hero.recipespace.domain.recipe.entity

data class RecipeResult(
    val recipe: RecipeEntity,
    val progress: () -> (Float),
)
