package com.hero.recipespace.data.recipe.remote

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.datastore.RecipeCloudStore
import kotlinx.coroutines.flow.Flow

class RecipeRemoteDataSourceImpl(
    private val recipeCloudStore: RecipeCloudStore
) : RecipeRemoteDataSource {
    override fun getData(recipeKey: String) : Flow<RecipeData> {
        return recipeCloudStore.getData(recipeKey)
    }

    override fun getDataList() : Flow<List<RecipeData>> {

        return recipeCloudStore.getDataList()
    }

    override suspend fun add(recipeData: RecipeData) {
        recipeCloudStore.add(recipeData)
    }

    override suspend fun update(
        recipeData: RecipeData
    ) {
        recipeCloudStore.update(recipeData)
    }

    override suspend fun remove(
        recipeData: RecipeData
    ) {
        recipeCloudStore.remove(recipeData)
    }
}