package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import kotlinx.coroutines.flow.Flow

interface RecipeRemoteDataSource {
    fun getData(recipeKey: String) : Flow<RecipeData>

    fun getDataList() : Flow<List<RecipeData>>

    suspend fun add(recipeData: RecipeData)

    suspend fun update(recipeData: RecipeData)

    suspend fun remove(recipeData: RecipeData)
}