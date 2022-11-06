package com.hero.recipespace.database

import com.hero.recipespace.listener.OnCompleteListener

interface DataStore<T> {
    fun getData(onCompleteListener: OnCompleteListener<T>, vararg params: Any?)
    fun getDataList(onCompleteListener: OnCompleteListener<List<T>>, vararg params: Any?)
    fun add(onCompleteListener: OnCompleteListener<T>, data: T)
    fun update(onCompleteListener: OnCompleteListener<T>, data: T)
    fun remove(onCompleteListener: OnCompleteListener<T>, data: T)
}