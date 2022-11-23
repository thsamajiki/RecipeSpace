package com.hero.recipespace.database.recipe

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

    override fun getRecipe(
        recipeKey: String,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun getRecipeList(onCompleteListener: OnCompleteListener<List<RecipeEntity>>) {
        TODO("Not yet implemented")
    }

    override fun addRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
//        val recipeData: RecipeData

        recipeRemoteDataSource.add(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun modifyRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
        recipeRemoteDataSource.update(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun deleteRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
        recipeRemoteDataSource.remove(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                TODO("Not yet implemented")
            }
        })
    }
}