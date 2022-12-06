package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class RecipeCloudStore() {

    companion object {
        private var instance : RecipeCloudStore? = null

        fun getInstance(context: Context) : RecipeCloudStore {
            return synchronized(this) {
                instance ?: RecipeCloudStore().also {
                    instance = it
                }
            }
        }
    }

    fun getData(recipeKey: String) : Flow<RecipeData> {
        return callbackFlow {

        }
    }

    fun getDataList(): Flow<List<RecipeData>> {

        return callbackFlow {
            FirebaseData.getInstance()
                .downloadRecipeData(object : OnCompleteListener<List<RecipeData>> {
                    override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>?) {
                        if (isSuccess && response?.isNotEmpty() == true) {
                            val recipeList = response.getData()
                                .orEmpty()

                            trySendBlocking(recipeList)
                        }
                    }
                })
        }
    }

    fun add(data: RecipeData) {
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

    fun update(data: RecipeData) {
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

    fun remove(data: RecipeData) {
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