package com.hero.recipespace.data.recipe.local

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.datastore.RecipeCacheStore
import com.hero.recipespace.database.recipe.datastore.RecipeLocalStore
import com.hero.recipespace.listener.OnCompleteListener

class RecipeLocalDataSourceImpl(
    private val recipeLocalStore: RecipeLocalStore,
    private val recipeCacheStore: RecipeCacheStore
) : RecipeLocalDataSource {
    override fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(onCompleteListener: OnCompleteListener<List<RecipeData>>
    ) {
        TODO("Not yet implemented")
    }

    override fun clear() {
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