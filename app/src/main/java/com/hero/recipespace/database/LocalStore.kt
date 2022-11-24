package com.hero.recipespace.database

import android.content.Context
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

abstract class LocalStore<T>(context: Context) : DataStore<T> {

    private val dataList: List<T> = ArrayList()

    fun getDataList(param: OnCompleteListener<List<RecipeData>>): List<T> {
        return dataList
    }

    fun addAll(dataList: List<T>?) {
        if (dataList == null) {
            return
        }
        for (element in dataList) {
            val index = this.dataList.indexOf(element)
            if (index == -1) {
                // create
                this.dataList.add(element)
            } else {
                // update
                this.dataList.set(index, element)
            }
        }
    }
}