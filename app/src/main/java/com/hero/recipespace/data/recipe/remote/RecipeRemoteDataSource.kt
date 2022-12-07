package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData

interface RecipeRemoteDataSource {
    suspend fun getData(recipeKey: String) : RecipeData

    suspend fun getDataList() : List<RecipeData>

    suspend fun add(recipeData: RecipeData)

    suspend fun update(recipeData: RecipeData)

    suspend fun remove(recipeData: RecipeData)
}