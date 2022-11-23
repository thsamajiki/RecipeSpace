package com.hero.recipespace.database

import android.content.Context

abstract class LocalStore<T>(context: Context) : DataStore<T> {

    private val dataList: List<T> = ArrayList()

    fun getDataList(): List<T> {
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