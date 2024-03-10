package com.hero.recipespace.data.recipe.local

import com.hero.recipespace.data.recipe.RecipeData
import kotlinx.coroutines.flow.Flow

interface RecipeLocalDataSource {
    suspend fun getData(recipeKey: String): RecipeData

    fun observeDataList(): Flow<List<RecipeData>>

    fun clear()

    suspend fun add(recipeData: RecipeData)

    suspend fun addAll(recipeList: List<RecipeData>)

    suspend fun update(recipeData: RecipeData)

    suspend fun remove(recipeData: RecipeData)
}
