package com.hero.recipespace.domain.recipe.repository

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeRepository {
    suspend fun getRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeEntity>): LiveData<RecipeEntity>

    fun getRecipeList(onCompleteListener: OnCompleteListener<List<RecipeEntity>>): LiveData<List<RecipeEntity>>

    suspend fun addRecipe(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>)

    suspend fun modifyRecipe(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>)

    suspend fun deleteRecipe(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>)
}