package com.hero.recipespace.data.recipe.service

import com.google.firebase.Timestamp
import com.hero.recipespace.data.recipe.RecipeData

interface RecipeService {
    suspend fun getRecipe(recipeKey: String): RecipeData
    
    suspend fun getRecipeList(): List<RecipeData>

    suspend fun add(profileImageUrl : String,
                    userName: String,
                    userKey: String,
                    desc: String,
                    photoUrlList: List<String>,
                    postDate: Timestamp
    ) : RecipeData
    
    suspend fun update(recipeData: RecipeData) : RecipeData
    
    suspend fun remove(recipeData: RecipeData) : RecipeData
}