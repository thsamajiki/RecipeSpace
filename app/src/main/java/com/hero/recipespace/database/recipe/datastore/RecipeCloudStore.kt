package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener
import java.lang.Exception

class RecipeCloudStore(
    private val context: Context,
    private val recipeLocalStore: RecipeLocalStore,
    private val recipeCacheStore: RecipeCacheStore
) : CloudStore<RecipeData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : RecipeCloudStore

        fun getInstance(context: Context) : RecipeCloudStore {
            return instance ?: synchronized(this) {
                instance ?: RecipeCloudStore(context, recipeLocalStore, recipeCacheStore).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<RecipeData> {
            override fun apply(transaction: Transaction): RecipeData {
                val recipeRef: DocumentReference = fireStore.collection("Recipe")
                    .document()

                data.key = recipeRef.id


                transaction.set(recipeRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<RecipeData> {
            override fun onSuccess(recipeData: RecipeData?) {
                onCompleteListener.onComplete(true, recipeData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun update(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<RecipeData> {
            override fun apply(transaction: Transaction): RecipeData {
                val recipeRef: DocumentReference = fireStore.collection("Recipe")
                    .document()

                data.key = recipeRef.id


                transaction.update(recipeRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<RecipeData> {
            override fun onSuccess(recipeData: RecipeData?) {
                onCompleteListener.onComplete(true, recipeData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun remove(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<RecipeData> {
            override fun apply(transaction: Transaction): RecipeData {
                val recipeRef: DocumentReference = fireStore.collection("Recipe")
                    .document()

                data.key = recipeRef.id


                transaction.delete(recipeRef)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<RecipeData> {
            override fun onSuccess(recipeData: RecipeData?) {
                onCompleteListener.onComplete(true, recipeData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }
}