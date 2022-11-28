package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeRemoteDataSource {
    suspend fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<RecipeData>>)

    suspend fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    suspend fun update(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    suspend fun remove(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)
}