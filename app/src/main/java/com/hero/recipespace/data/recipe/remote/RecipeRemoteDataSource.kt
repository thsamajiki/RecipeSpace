package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.recipe.request.UpdateRecipeRequest
import com.hero.recipespace.domain.recipe.request.UploadRecipeRequest

interface RecipeRemoteDataSource {
    suspend fun getData(recipeKey: String): RecipeData

    suspend fun getDataList(): List<RecipeData>

    suspend fun add(
        request: UploadRecipeRequest,
        onProgress: (Float) -> Unit,
    ): RecipeData

    suspend fun update(
        request: UpdateRecipeRequest,
        onProgress: (Float) -> Unit,
    ): RecipeData

    suspend fun remove(recipeData: RecipeData): RecipeData
}
