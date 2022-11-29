package com.hero.recipespace.database

import android.content.Context
import com.hero.recipespace.listener.OnCompleteListener

abstract class LocalStore<T>(context: Context) : DataStore<T> {

    private val dataList: MutableList<T> = mutableListOf()

    fun getDataList(param: OnCompleteListener<List<T>>): List<T> {
        return dataList
    }

    fun addAll(dataList: List<T>?) {
        if (dataList == null) {
            return
        }
        for (element: T in dataList) {
            val index = this.dataList.indexOf(element)
            if (index == -1) {
                // create
                this.dataList.add(element)
            } else {
                // update
                this.dataList[index] = element
            }
        }
    }
}