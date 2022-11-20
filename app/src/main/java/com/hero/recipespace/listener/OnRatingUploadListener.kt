package com.hero.recipespace.listener

import com.hero.recipespace.data.recipe.RecipeData

interface OnRatingUploadListener {
    fun onRatingUpload(recipeData: RecipeData?)
}