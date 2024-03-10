package com.hero.recipespace.data.recipe.service

import android.net.Uri
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.util.WLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeServiceImpl
@Inject
constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : RecipeService {
    override suspend fun getRecipe(recipeKey: String): RecipeData {
        val rateAverage = getRecipeRateAverage(recipeKey)

        return suspendCoroutine { continuation ->
            db.collection("Recipe")
                .document(recipeKey)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot == null) {
                        return@addOnSuccessListener
                    }

                    val recipeData =
                        documentSnapshot.toObject(RecipeData::class.java)
                            ?.copy(rate = rateAverage)

                    continuation.resume(recipeData!!)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    private suspend fun getRecipeRateAverage(recipeKey: String): Float {
        return suspendCoroutine { continuation ->
            val recipeRef =
                db.collection("Recipe")
                    .document(recipeKey)
            val rateRef = recipeRef.collection("RateList")

            rateRef.get()
                .addOnSuccessListener { querySnapShot ->
                    val rateDataList =
                        querySnapShot.documents.mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(RateData::class.java)
                        }

                    WLog.d("rateDataList $rateDataList")

                    val rateAverage =
                        if (rateDataList.isNotEmpty()) {
                            rateDataList.sumOf {
                                it.rate?.toInt() ?: 0
                            } / rateDataList.size.toFloat()
                        } else {
                            0f
                        }

                    continuation.resume(rateAverage)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getRecipeList(): List<RecipeData> {
        return suspendCoroutine { continuation ->
            db.collection("Recipe")
                .orderBy(
                    "postDate",
                    Query.Direction.DESCENDING,
                )
                .get()
                .addOnSuccessListener(
                    OnSuccessListener { queryDocumentSnapshots ->
                        if (queryDocumentSnapshots.isEmpty) {
                            return@OnSuccessListener
                        }
                        val recipeDataList =
                            queryDocumentSnapshots.documents
                                .mapNotNull { documentSnapshot ->
                                    documentSnapshot.toObject(RecipeData::class.java)
                                }

                        CoroutineScope(Dispatchers.Main).launch {
                            val result =
                                recipeDataList.map { recipe ->
                                    async {
                                        val rateAverage = getRecipeRateAverage(recipe.key)
                                        recipe.copy(rate = rateAverage)
                                    }
                                }
                                    .awaitAll()

                            continuation.resume(result)
                            WLog.d("result $result")
                            cancel()
                        }
                    },
                )
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getMyRecipeList(userKey: String): List<RecipeData> {
        return suspendCoroutine { continuation ->
            db.collection("Recipe")
                .whereEqualTo(
                    "userKey",
                    userKey,
                )
                .get()
                .addOnSuccessListener(
                    OnSuccessListener { queryDocumentSnapshots ->
                        if (queryDocumentSnapshots.isEmpty) {
                            return@OnSuccessListener
                        }
                        val recipeDataList =
                            queryDocumentSnapshots.documents
                                .mapNotNull { documentSnapshot ->
                                    documentSnapshot.toObject(RecipeData::class.java)
                                }

                        CoroutineScope(Dispatchers.Main).launch {
                            val result =
                                recipeDataList.map { recipe ->
                                    async {
                                        val rateAverage = getRecipeRateAverage(recipe.key)
                                        recipe.copy(rate = rateAverage)
                                    }
                                }
                                    .awaitAll()

                            continuation.resume(result)

                            cancel()
                        }
                    },
                )
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
        profileImageUrl: String,
    ): RecipeData {
        return suspendCoroutine<RecipeData> { continuation ->
            val documentReference =
                db.collection("Recipe").document()
            val key = documentReference.id
            val recipeData =
                RecipeData(
                    key,
                    profileImageUrl,
                    userName,
                    userKey,
                    desc,
                    photoUrlList,
                    postDate,
                )

            documentReference.set(recipeData)
                .addOnSuccessListener { continuation.resume(recipeData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun uploadImages(
        recipePhotoPathList: List<String>,
        progress: (Float) -> Unit,
    ): List<String> {
        val oldImageList =
            recipePhotoPathList.filter {
                it.startsWith("https://") || it.startsWith("http://")
            }

        val pathList =
            recipePhotoPathList.filterNot {
                it.startsWith("https://") || it.startsWith("http://")
            }

        val imageFolderRef = storage.reference.child(DEFAULT_IMAGE_PATH)

        return withContext(Dispatchers.IO) {
            val newImageList =
                pathList.map { photoPath ->
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
        onProgress: (Float) -> Unit,
    ): RecipeData {
        val editData = HashMap<String, Any>()

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
