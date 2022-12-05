package com.hero.recipespace.data.recipe.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

interface RecipeLocalDataSource {
    suspend fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) : LiveData<RecipeData>

    fun getDataList(onCompleteListener: OnCompleteListener<List<RecipeData>>) : LiveData<List<RecipeData>>

    fun clear()

    suspend fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    suspend fun update(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)

    suspend fun remove(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>)
}