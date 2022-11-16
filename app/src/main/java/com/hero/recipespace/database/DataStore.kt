package com.hero.recipespace.database

import com.hero.recipespace.listener.OnCompleteListener

interface DataStore<T> {
    fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<T>)
    fun getDataList(vararg params: Any?, onCompleteListener: OnCompleteListener<List<T>>)
    fun add(data: T, onCompleteListener: OnCompleteListener<T>)
    fun update(data: T, onCompleteListener: OnCompleteListener<T>)
    fun remove(data: T, onCompleteListener: OnCompleteListener<T>)
}