package com.hero.recipespace.data.recipe.local

import androidx.lifecycle.asFlow
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.dao.RecipeDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeLocalDataSource {

    override suspend fun getData(recipeKey: String) : RecipeData {
        return recipeDao.getRecipeFromKey(recipeKey) ?: error("not found RecipeData")
    }

    override fun observeDataList() : Flow<List<RecipeData>> {
        return recipeDao.observeAllRecipes().asFlow()
    }

    override suspend fun add(recipeData: RecipeData) {
        recipeDao.insertRecipe(recipeData)
    }

    override suspend fun addAll(recipeList: List<RecipeData>) {
        recipeDao.insertAll(recipeList)
    }

    override suspend fun update(
        recipeData: RecipeData
    ) {
        recipeDao.updateRecipe(recipeData)
    }

    override suspend fun remove(
        recipeData: RecipeData
    ) {
        recipeDao.deleteRecipe(recipeData)
    }

    override fun clear() {

    }
}