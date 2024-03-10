package com.hero.recipespace.data.recipe.service

import com.google.firebase.Timestamp
import com.hero.recipespace.data.recipe.RecipeData

interface RecipeService {
    suspend fun getRecipe(recipeKey: String): RecipeData

    suspend fun getRecipeList(): List<RecipeData>

    suspend fun getMyRecipeList(userKey: String): List<RecipeData>

    suspend fun add(
        desc: String,
        photoUrlList: List<String>,
        postDate: Timestamp,
        userKey: String,
        userName: String,
        profileImageUrl: String,
    ): RecipeData

    suspend fun uploadImages(
        recipePhotoPathList: List<String>,
        progress: (Float) -> Unit,
    ): List<String>

    suspend fun update(
        key: String,
        content: String,
        photoUrlList: List<String>,
        onProgress: (Float) -> Unit,
    ): RecipeData

    suspend fun remove(recipeData: RecipeData): RecipeData
}
