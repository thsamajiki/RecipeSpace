package com.hero.recipespace.database.recipe

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.local.RecipeLocalDataSource
import com.hero.recipespace.data.recipe.remote.RecipeRemoteDataSource
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFailedListener

class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
    private val recipeLocalDataSource: RecipeLocalDataSource,
) : RecipeRepository {


    fun addUser(
        onCompleteListener: OnCompleteListener<UserEntity?>,
        onFailedListener: OnFailedListener?,
        userName: String?,
        email: String?,
        pwd: String?,
    ) {
        userRemoteDataSource.addUser(object : OnCompleteListener<UserData?>() {
            override fun onComplete(isSuccess: Boolean, remoteData: UserData) {
                if (isSuccess) {
                    userLocalDataSource.addUser(object : OnCompleteListener<UserData?>() {
                        override fun onComplete(isSuccess: Boolean, localData: UserData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity())
                            } else {
                                onCompleteListener.onComplete(true, remoteData.toEntity())
                            }
                        }
                    }, onFailedListener, remoteData)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        }, onFailedListener, userName, email, pwd)
    }

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

        })
    }

    override fun modifyRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteRecipe(
        recipeEntity: RecipeEntity,
        onCompleteListener: OnCompleteListener<RecipeEntity>,
    ) {
        TODO("Not yet implemented")
    }
}