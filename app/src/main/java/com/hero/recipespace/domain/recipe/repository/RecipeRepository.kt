package com.hero.recipespace.domain.recipe.repository

import com.google.firebase.Timestamp
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipe(recipeKey: String): RecipeEntity

    fun observeRecipeList(): Flow<List<RecipeEntity>>

    suspend fun addRecipe(profileImageUrl : String,
                          userName: String,
                          userKey: String,
                          desc: String,
                          photoUrlList: List<String>,
                          postDate: Timestamp
    )

    suspend fun modifyRecipe(recipeEntity: RecipeEntity)

    suspend fun deleteRecipe(recipeEntity: RecipeEntity)
}