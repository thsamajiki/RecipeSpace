package com.hero.recipespace.data.recipe.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.datastore.RecipeCacheStore
import com.hero.recipespace.database.recipe.datastore.RecipeLocalStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import kotlinx.coroutines.flow.Flow

class RecipeLocalDataSourceImpl(
    private val recipeLocalStore: RecipeLocalStore,
    private val recipeCacheStore: RecipeCacheStore
) : RecipeLocalDataSource {

    override suspend fun getData(recipeKey: String) : Flow<RecipeData> {
        recipeCacheStore.getData(recipeKey, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getData(recipeKey, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun getDataList() : Flow<List<RecipeData>> {

    }

    override fun clear() {
        recipeCacheStore.clear()
    }

    override suspend fun add(recipeData: RecipeData) {

    }

    override suspend fun update(
        recipeData: RecipeData
    ) {

    }

    override suspend fun remove(
        recipeData: RecipeData
    ) {
        recipeLocalStore.remove(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeCacheStore.remove(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
//                                onCompleteListener.onComplete(true, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }
}