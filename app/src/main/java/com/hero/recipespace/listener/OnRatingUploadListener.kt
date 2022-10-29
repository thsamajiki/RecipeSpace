package com.hero.recipespace.listener

import com.hero.recipespace.data.RecipeData

interface OnRatingUploadListener {
    fun onRatingUpload(recipeData: RecipeData?)
}