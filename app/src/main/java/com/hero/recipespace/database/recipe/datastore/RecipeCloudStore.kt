package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class RecipeCloudStore(
    private val context: Context,
    private val recipeLocalStore: RecipeLocalStore,
    private val recipeCacheStore: RecipeCacheStore
) : CloudStore<RecipeData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : RecipeCloudStore

        fun getInstance(context: Context) : RecipeCloudStore {
            return instance ?: synchronized(this) {
                instance ?: RecipeCloudStore(context, recipeLocalStore, recipeCacheStore).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }
}