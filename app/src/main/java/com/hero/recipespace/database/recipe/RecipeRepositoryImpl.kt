package com.hero.recipespace.database.recipe

import android.widget.Toast
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.local.RecipeLocalDataSource
import com.hero.recipespace.data.recipe.remote.RecipeRemoteDataSource
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
    private val recipeLocalDataSource: RecipeLocalDataSource,
) : RecipeRepository {

    override suspend fun getRecipe(
        recipeKey: String,
        onCompleteListener: OnCompleteListener<RecipeEntity>
    ) {
        recipeLocalDataSource.getData(recipeKey, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {

                } else {
                    recipeRemoteDataSource.getData(recipeKey, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                            if (isSuccess) {

                            } else {
                                Toast.makeText(this, "레시피를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        })
    }

    override fun getRecipeList(onCompleteListener: OnCompleteListener<List<RecipeEntity>>) {
        recipeLocalDataSource.getDataList(object : OnCompleteListener<List<RecipeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>?) {
                if (isSuccess) {

                } else {
                    recipeRemoteDataSource.getDataList(object : OnCompleteListener<List<RecipeData>> {
                        override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>?) {
                            if (isSuccess) {

                            } else {
                                Toast.makeText(this, "레시피를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        })
    }

    override suspend fun addRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
//        val recipeData: RecipeData

        recipeRemoteDataSource.add(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeLocalDataSource.add(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                } else {
                    Toast.makeText(this, "레시피를 생성하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    override suspend fun modifyRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
        recipeRemoteDataSource.update(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeLocalDataSource.update(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                            if (isSuccess) {

                            } else {
                                Toast.makeText(this, "레시피를 변경하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(this, "레시피를 변경하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override suspend fun deleteRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
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