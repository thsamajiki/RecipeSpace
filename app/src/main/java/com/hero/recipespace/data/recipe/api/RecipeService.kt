package com.hero.recipespace.data.recipe.api

import com.hero.recipespace.data.recipe.RecipeData

interface RecipeService {
    suspend fun getRecipe(recipeKey: String): RecipeData
    
    suspend fun getRecipeList(): List<RecipeData>

    suspend fun add(recipeData: RecipeData)
    
    suspend fun update(recipeData: RecipeData)
    
    suspend fun remove(recipeData: RecipeData)
}