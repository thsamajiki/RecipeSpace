package com.hero.recipespace.database

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.listener.OnCompleteListener

abstract class CloudStore<T>(
    private val context: Context,
    private val fireStore: FirebaseFirestore
) : DataStore<T> {
    abstract fun getData(key: String, onCompleteListener: OnCompleteListener<T>)

    abstract fun getDataList(onCompleteListener: OnCompleteListener<List<T>>)

    abstract fun getDataList(key: String, onCompleteListener: OnCompleteListener<List<T>>)
}