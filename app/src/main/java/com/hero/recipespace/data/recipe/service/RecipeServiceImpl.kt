package com.hero.recipespace.data.recipe.service

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeServiceImpl @Inject constructor(): RecipeService {
    override suspend fun getRecipe(recipeKey: String): RecipeData {
        return suspendCoroutine { continuation ->
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("RecipeData")
                .document(recipeKey)
                .get()
                .addOnSuccessListener(OnSuccessListener { queryDocumentSnapshots ->
                    if (queryDocumentSnapshots.isEmpty()) {
                        return@OnSuccessListener
                    }
                    val recipeData = queryDocumentSnapshots.data
                        ?.mapNotNull { documentSnapshot ->
                            documentSnapshot.apply {

                            }
//                            documentSnapshot.toObject(RecipeData::class.java)
                        }

                    continuation.resume(recipeData)
                })
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getRecipeList(): List<RecipeData> {
        return suspendCoroutine { continuation ->
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("RecipeData")
                .orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(OnSuccessListener { queryDocumentSnapshots ->
                    if (queryDocumentSnapshots.isEmpty) {
                        return@OnSuccessListener
                    }
                    val recipeDataList = queryDocumentSnapshots.documents
                        .mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(RecipeData::class.java)
                        }

                    continuation.resume(recipeDataList)
                })
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun add(recipeData: RecipeData) {
        suspendCoroutine<RecipeData> {
            val fireStore = FirebaseFirestore.getInstance()
            val documentReference = fireStore.collection("RecipeData").document()
            val key = documentReference.id
            recipeData.key = key

            documentReference.set(recipeData)
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun update(recipeData: RecipeData) {
        suspendCoroutine<RecipeData> {
            val response: Response<Void> = Response()
            response.setType(Type.FIRE_STORE)
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("RecipeData")
                .document(recipeData.key!!)
                .update(editData)
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun remove(recipeData: RecipeData) {
        suspendCoroutine<RecipeData> {
            val response: Response<Void> = Response()
            response.setType(Type.FIRE_STORE)
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("RecipeData")
                .document(recipeData.key!!)
                .delete()
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}