package com.hero.recipespace.data.recipe.service

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
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

class RecipeServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): RecipeService {
    override suspend fun getRecipe(recipeKey: String): RecipeData {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("RecipeData")
                .document(recipeKey)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    if (queryDocumentSnapshots.isEmpty()) {
                        return@OnSuccessListener
                    }
//                    val recipeData = queryDocumentSnapshots.data
//                        ?.mapNotNull { documentSnapshot ->
//                            documentSnapshot.apply {
//
//                            }
//                            documentSnapshot.toObject(RecipeData::class.java)
//                        }
                    val recipeData = queryDocumentSnapshots.data?.getValue(recipeKey)

                    continuation.resume(recipeData as RecipeData)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getRecipeList(): List<RecipeData> {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("RecipeData")
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

    override suspend fun add(profileImageUrl : String,
                             userName: String,
                             userKey: String,
                             desc: String,
                             photoUrlList: List<String>,
                             postDate: Timestamp
    ) : RecipeData {
        return suspendCoroutine<RecipeData> { continuation ->
            val documentReference = firebaseFirestore.collection("RecipeData").document()
            val key = documentReference.id
            val recipeData = RecipeData(
                key,
                profileImageUrl,
                userName,
                userKey,
                desc,
                photoUrlList,
                postDate
            )

            documentReference.set(recipeData)
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun update(recipeData: RecipeData) : RecipeData {
        return suspendCoroutine<RecipeData> { continuation ->
            firebaseFirestore.collection("RecipeData")
                .document(recipeData.key.orEmpty())
                .update(editData)
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun remove(recipeData: RecipeData) : RecipeData {
        return suspendCoroutine<RecipeData> { continuation ->
            firebaseFirestore.collection("RecipeData")
                .document(recipeData.key.orEmpty())
                .delete()
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}