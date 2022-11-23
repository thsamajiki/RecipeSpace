package com.hero.recipespace.domain.recipe.repository

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeRepository {
    fun getRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)

    fun getRecipeList(onCompleteListener: OnCompleteListener<List<RecipeData>>)

    fun addRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)

    fun modifyRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)

    fun deleteRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)
}