package com.hero.recipespace.database

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.listener.OnCompleteListener

abstract class CacheStore<T> : DataStore<T> {
    private var dataList: MutableList<T> = ArrayList<T>()

    fun getDataList(param: OnCompleteListener<List<RecipeData>>): List<T> {
        return dataList
    }

    fun setDataList(dataList: ArrayList<T>) {
        this.dataList = dataList
    }

    fun add(data: T) {
        if (!dataList.contains(data)) {
            dataList.add(data)
        }
    }

    fun add(index: Int, data: T) {
        if (!dataList.contains(data)) {
            dataList.add(index, data)
        }
    }

    fun addAll(dataList: List<T?>?) {
        if (dataList == null) {
            return
        }
        for (element in dataList) {
            val index = this.dataList.indexOf(element)
            if (index != -1) {
                this.dataList[index] = element
            } else {
                this.dataList.add(element)
            }
        }
    }

    fun clear() {
        dataList.clear()
    }
}