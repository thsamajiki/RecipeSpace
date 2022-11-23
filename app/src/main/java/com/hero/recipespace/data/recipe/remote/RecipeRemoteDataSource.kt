package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeRemoteDataSource {
    fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)

    fun getDataList(recipeKey: String, onCompleteListener: OnCompleteListener<List<RecipeData>>)

    fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    fun update(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    fun remove(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)
}