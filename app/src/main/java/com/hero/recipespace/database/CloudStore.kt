package com.hero.recipespace.database

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

abstract class CloudStore<T>(
    private val context: Context,
    private val fireStore: FirebaseFirestore
) : DataStore<T> {

}