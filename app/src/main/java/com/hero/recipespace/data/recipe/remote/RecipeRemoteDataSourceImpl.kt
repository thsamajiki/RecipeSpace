package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.datastore.RecipeCloudStore
import com.hero.recipespace.listener.OnCompleteListener

class RecipeRemoteDataSourceImpl(
    private val recipeCloudStore: RecipeCloudStore
) : RecipeRemoteDataSource {
    override fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeCloudStore.getData(recipeKey, onCompleteListener)
    }

    override fun getDataList(onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        recipeCloudStore.getDataList(onCompleteListener)
    }

    override fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeCloudStore.add(recipeData, onCompleteListener)
    }

    override fun update(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        recipeCloudStore.update(recipeData, onCompleteListener)
    }

    override fun remove(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        recipeCloudStore.remove(recipeData, onCompleteListener)
    }
}