package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.CacheStore
import com.hero.recipespace.listener.OnCompleteListener

class RecipeCacheStore : CacheStore<RecipeData>() {

    companion object {
        private lateinit var instance : RecipeCacheStore

        fun getInstance(context: Context) : RecipeCacheStore {
            return instance ?: synchronized(this) {
                instance ?: RecipeCacheStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RecipeData>) {
        val recipeKey: String = params[0].toString()

        for (recipeData in getDataList()) {
            if (recipeData.key.equals(recipeKey)) {
                onCompleteListener.onComplete(true, recipeData)
                return
            }
        }
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        if (getDataList().isEmpty()) {
            onCompleteListener.onComplete(true, null)
        } else {
            onCompleteListener.onComplete(false, getDataList())
        }
    }

    override fun add(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        getDataList().add(data)

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data)
        }
    }

    override fun update(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val originIndex = getDataList().indexOf(data)
        if (originIndex == -1) {
            throw IndexOutOfBoundsException("기존 데이터가 없습니다.")
        } else {
            getDataList().set(originIndex, data)
        }

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data)
        }
    }

    override fun remove(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val originIndex = getDataList().indexOf(data)
        if (originIndex == -1) {
            throw IndexOutOfBoundsException("기존 데이터가 없습니다.")
        } else {
            getDataList().remove(originIndex)
        }
    }
}