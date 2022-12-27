package com.hero.recipespace.data.recipe.service

import android.net.Uri
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.util.WLog
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : RecipeService {

    override suspend fun getRecipe(recipeKey: String): RecipeData {
        return suspendCoroutine { continuation ->
            db.collection("Recipe")
                .document(recipeKey)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot == null) {
                        return@addOnSuccessListener
                    }

                    val recipeData = documentSnapshot.toObject(RecipeData::class.java)

                    continuation.resume(recipeData!!)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getRecipeList(): List<RecipeData> {
        return suspendCoroutine { continuation ->
            db.collection("Recipe")
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

    override suspend fun add(
        desc: String,
        photoUrlList: List<String>,
        postDate: Timestamp,
        userKey: String,
        userName: String,
        profileImageUrl : String
    ): RecipeData {
        return suspendCoroutine<RecipeData> { continuation ->
            val documentReference = db.collection("Recipe").document()
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

    override suspend fun uploadImages(
        recipePhotoPathList: List<String>,
        progress: (Float) -> Unit
    ): List<String> {
        val oldImageList = recipePhotoPathList.filter {
            it.startsWith("https://") || it.startsWith("http://")
        }

        val pathList = recipePhotoPathList.filterNot {
            it.startsWith("https://") || it.startsWith("http://")
        }

        val imageFolderRef = firebaseStorage.reference.child(DEFAULT_IMAGE_PATH)

        return withContext(Dispatchers.IO) {
            val newImageList = pathList.map { photoPath ->
                val imageRef = imageFolderRef.child("${Uri.parse(photoPath).lastPathSegment}")

                async {
                    kotlin.runCatching {
                        val imageFile = Uri.parse(photoPath)
                        val uploadTask = imageRef.putFile(imageFile)

                        uploadTask
                            .await()
                            .storage
                            .downloadUrl
                            .await()
                            .toString()
                    }
                        .onFailure {
                            WLog.e(it)
                        }
                }
            }
                .awaitAll()
                .mapNotNull {
                    it.getOrNull()
                }

            oldImageList + newImageList
        }
    }

    override suspend fun update(
        key: String,
        content: String,
        photoUrlList: List<String>,
        onProgress: (Float) -> Unit
    ): RecipeData {
        val editData = HashMap<String, Any>()

        // FIXME: 2022-12-15 PhotoList 에 갱신된 데이터 넣는 것 수정하기
        editData["desc"] = content
        editData["photoUrlList"] = photoUrlList

        return suspendCoroutine<RecipeData> { continuation ->
            db.collection("Recipe")
                .document(key)
                .update(editData)
                .addOnSuccessListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        continuation.resume(getRecipe(key))
                        cancel()
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun remove(recipeData: RecipeData): RecipeData {
        return suspendCoroutine<RecipeData> { continuation ->
            db.collection("Recipe")
                .document(recipeData.key.orEmpty())
                .delete()
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    companion object {
        const val DEFAULT_IMAGE_PATH = "images/"
        const val PROFILE_IMAGE_PATH = "profile/"
    }
}