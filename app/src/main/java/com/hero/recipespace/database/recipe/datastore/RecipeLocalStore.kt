package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.database.recipe.dao.RecipeDao
import com.hero.recipespace.listener.OnCompleteListener

class RecipeLocalStore(
    private val context: Context,
    private val recipeDao: RecipeDao
) : LocalStore<RecipeData>(context) {

    companion object {
        private lateinit var instance : RecipeLocalStore

        fun getInstance(context: Context, recipeDao: RecipeDao) : RecipeLocalStore {
            return instance ?: synchronized(this) {
                instance ?: RecipeLocalStore(context, recipeDao).also {
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

        val recipeKey: String = params[0].toString()
        val recipeData: RecipeData = recipeDao.getRecipeFromKey(recipeKey)!!
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }

        val recipeDataList: LiveData<List<RecipeData>> = recipeDao.getAllRecipes()

        onCompleteListener.onComplete(true, recipeDataList)
    }

    override fun add(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeDao.insertRecipe(data)

        onCompleteListener.onComplete(true, data)
    }

    override fun update(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeDao.updateRecipe(data)

        onCompleteListener.onComplete(true, data)
    }

    override fun remove(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeDao.deleteRecipe(data)

        onCompleteListener.onComplete(true, data)
    }
}