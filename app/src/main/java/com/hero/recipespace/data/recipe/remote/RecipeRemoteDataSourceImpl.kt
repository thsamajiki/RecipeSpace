package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

class RecipeRemoteDataSourceImpl : RecipeRemoteDataSource {
    override fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        recipeKey: String,
        onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun update(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        TODO("Not yet implemented")
    }

    override fun remove(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        TODO("Not yet implemented")
    }
}