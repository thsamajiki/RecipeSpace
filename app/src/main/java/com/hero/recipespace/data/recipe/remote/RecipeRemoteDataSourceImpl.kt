package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.api.RecipeService
import javax.inject.Inject

class RecipeRemoteDataSourceImpl @Inject constructor(
    private val recipeService: RecipeService
) : RecipeRemoteDataSource {
    override suspend fun getData(recipeKey: String) : RecipeData {
        return recipeService.getRecipe(recipeKey)
    }

    override suspend fun getDataList() : List<RecipeData> {
        return recipeService.getRecipeList()
    }

    override suspend fun add(recipeData: RecipeData) {
        recipeService.add(recipeData)
    }

    override suspend fun update(
        recipeData: RecipeData
    ) {
        recipeService.update(recipeData)
    }

    override suspend fun remove(
        recipeData: RecipeData
    ) {
        recipeService.remove(recipeData)
    }
}