package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.listener.OnCompleteListener

class RecipeLocalStore(
    private val context: Context
) : LocalStore<RecipeData>(context) {

    companion object {
        private lateinit var instance : RecipeLocalStore

        fun getInstance(context: Context) : RecipeLocalStore {
            return instance ?: synchronized(this) {
                instance ?: RecipeLocalStore(context).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RecipeData>) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }
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