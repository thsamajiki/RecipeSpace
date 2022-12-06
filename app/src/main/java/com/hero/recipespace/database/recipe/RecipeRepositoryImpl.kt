package com.hero.recipespace.database.recipe

import android.widget.Toast
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.local.RecipeLocalDataSource
import com.hero.recipespace.data.recipe.remote.RecipeRemoteDataSource
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.mapper.toData
import com.hero.recipespace.domain.recipe.mapper.toEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
    private val recipeLocalDataSource: RecipeLocalDataSource
) : RecipeRepository {

    override fun getRecipe(recipeKey: String) : Flow<RecipeEntity> {

        return recipeRemoteDataSource.getData(recipeKey)
            .map {
                    it.toEntity()
            }
    }

    override fun getRecipeList(): Flow<List<RecipeEntity>> {
        return recipeRemoteDataSource.getDataList()
            .map {
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addRecipe(
        recipeEntity: RecipeEntity
    ) {
//        val recipeData: RecipeData

        recipeRemoteDataSource.add(recipeEntity.toData(recipeEntity))
    }

    override suspend fun modifyRecipe(
        recipeEntity: RecipeEntity
    ) {

    }

    override suspend fun deleteRecipe(
        recipeEntity: RecipeEntity
    ) {
        recipeRemoteDataSource.remove(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeLocalDataSource.remove(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                            if (isSuccess) {

                            } else {
                                Toast.makeText(this, "레시피를 삭제하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(this, "레시피를 삭제하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}