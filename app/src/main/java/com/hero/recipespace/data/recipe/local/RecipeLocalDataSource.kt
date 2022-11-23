package com.hero.recipespace.data.recipe.local

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeLocalDataSource {
    fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>)

    fun getDataList(recipeKey: String, onCompleteListener: OnCompleteListener<List<RecipeData>>)

    fun clear()

    fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    fun update(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    fun remove(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)
}