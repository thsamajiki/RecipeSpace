package com.hero.recipespace.domain.recipe.repository

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeRepository {
    fun getRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeEntity>)

    fun getRecipeList(onCompleteListener: OnCompleteListener<List<RecipeEntity>>)

    fun addRecipe(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>)

    fun modifyRecipe(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>)

    fun deleteRecipe(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>)
}