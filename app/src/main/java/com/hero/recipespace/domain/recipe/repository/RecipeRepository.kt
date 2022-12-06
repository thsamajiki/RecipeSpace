package com.hero.recipespace.domain.recipe.repository

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipe(recipeKey: String): Flow<RecipeEntity>

    fun getRecipeList(): Flow<List<RecipeEntity>>

    suspend fun addRecipe(recipeEntity: RecipeEntity)

    suspend fun modifyRecipe(recipeEntity: RecipeEntity)

    suspend fun deleteRecipe(recipeEntity: RecipeEntity)
}