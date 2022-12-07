package com.hero.recipespace.database.recipe

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.local.RecipeLocalDataSource
import com.hero.recipespace.data.recipe.remote.RecipeRemoteDataSource
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.mapper.toData
import com.hero.recipespace.domain.recipe.mapper.toEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
    private val recipeLocalDataSource: RecipeLocalDataSource
) : RecipeRepository {

    override suspend fun getRecipe(recipeKey: String) : RecipeEntity {
        return recipeLocalDataSource.getData(recipeKey).toEntity()
    }

    override fun observeRecipeList(): Flow<List<RecipeEntity>> {
        // SSOT - Single Source Of Truth
        // 지금까지 나는 ViewModel -> get -> Repository -> if(로컬에 데이터가 있는가?) local.getData() else remote.getData

        // SSOT 는
        // ViewModel -> get -> Repository -> remote.requestData() -> Remote -> Repository -> Local에 저장

        CoroutineScope(Dispatchers.IO).launch {
            val recipeList = recipeRemoteDataSource.getDataList()
            recipeLocalDataSource.addAll(recipeList)
            cancel()
        }

        return recipeLocalDataSource.observeDataList()
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addRecipe(
        recipeEntity: RecipeEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeRemoteDataSource.add(recipeEntity.toData())
            recipeLocalDataSource.add(recipeEntity.toData())
            cancel()
        }
    }

    override suspend fun modifyRecipe(
        recipeEntity: RecipeEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeRemoteDataSource.update(recipeEntity.toData())
            recipeLocalDataSource.update(recipeEntity.toData())
            cancel()
        }
    }

    override suspend fun deleteRecipe(
        recipeEntity: RecipeEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeRemoteDataSource.remove(recipeEntity.toData())
            recipeLocalDataSource.remove(recipeEntity.toData())
            cancel()
        }
    }

    private fun getEntities(data: List<RecipeData>): List<RecipeEntity> {
        val result = mutableListOf<RecipeEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}