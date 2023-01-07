package com.hero.recipespace.domain.recipe.repository

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.request.UpdateRecipeRequest
import com.hero.recipespace.domain.recipe.request.UploadRecipeRequest
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipe(recipeKey: String): RecipeEntity

    fun observeRecipeList(): Flow<List<RecipeEntity>>

    suspend fun refresh()

    suspend fun addRecipe(request: UploadRecipeRequest, onProgress: (Float) -> Unit) : RecipeEntity

    suspend fun modifyRecipe(request: UpdateRecipeRequest, onProgress: (Float) -> Unit) : RecipeEntity

    suspend fun deleteRecipe(recipeEntity: RecipeEntity)
}