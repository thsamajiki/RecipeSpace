package com.hero.recipespace.database

import com.hero.recipespace.listener.OnCompleteListener

interface DataStore<T> {
    suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<T>)
    fun getDataList(vararg params: Any, onCompleteListener: OnCompleteListener<List<T>>)
    suspend fun add(data: T, onCompleteListener: OnCompleteListener<T>)
    suspend fun update(data: T, onCompleteListener: OnCompleteListener<T>)
    suspend fun remove(data: T, onCompleteListener: OnCompleteListener<T>)
}