package com.hero.recipespace.data.recipe.service

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.recipe.request.UpdateRecipeRequest
import com.hero.recipespace.util.WLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
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
                .addOnSuccessListener { documentSnapShot ->
                    if (documentSnapShot == null) {
                        return@addOnSuccessListener
                    }
//                    val recipeData = queryDocumentSnapshots.data
//                        ?.mapNotNull { documentSnapshot ->
//                            documentSnapshot.apply {
//
//                            }
//                            documentSnapshot.toObject(RecipeData::class.java)
//                        }
                    val recipeData = documentSnapShot.data?.getValue(recipeKey)

                    continuation.resume(recipeData as RecipeData)
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
        profileImageUrl: String,
        userName: String,
        userKey: String,
        desc: String,
        photoUrlList: List<String>,
        postDate: Timestamp
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
        val storageRef =
            FirebaseStorage.getInstance().reference.child(DEFAULT_IMAGE_PATH)

        return withContext(Dispatchers.IO) {
            recipePhotoPathList.map { photoPath ->
                val photoRef =
                    storageRef.child(DEFAULT_IMAGE_PATH + Uri.parse(photoPath).lastPathSegment)

                async {
                    kotlin.runCatching {
                        photoRef
                            .putFile(Uri.fromFile(File(photoPath)))
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
        }

//        return suspendCoroutine { continuation ->
//
//            val imageFolderRef =
//                FirebaseStorage.getInstance().reference.child(DEFAULT_IMAGE_PATH)
//
//            val totalPhotoList: MutableList<String> = mutableListOf()
//
//            for (imageCount: Int in recipeImagePathList.indices) {
//                val imagePath: String = recipeImagePathList[imageCount]
//                val imageFileRef =
//                    imageFolderRef.child(DEFAULT_IMAGE_PATH + Uri.parse(imagePath).lastPathSegment)
//
//                val uploadTask = imageFileRef.putFile(Uri.fromFile(File(imagePath)))
//
//                uploadTask
//                    .addOnProgressListener { taskSnapshot ->
//                        val percent =
//                            taskSnapshot.bytesTransferred.toFloat() / taskSnapshot.totalByteCount.toFloat() * 100
//                        progress(percent)
//                    }
//                    .continueWithTask { task ->
//                        if (!task.isSuccessful) {
//                            throw task.exception!!
//                        }
//
//                        imageFolderRef.downloadUrl
//                    }
//                    .addOnSuccessListener { uri ->
////                        val photoDbRef =
////                            firebaseStorage.reference.child(recipePhotoPathList[imageCount])
////                        val photoMap: HashMap<String, Any> = HashMap()
////                        photoMap["photoUrl"] = uri
////
////                        photoRef.downloadUrl.addOnSuccessListener {
////                            totalPhotoList.add(uri.toString())
////
////                            if (totalPhotoList.size == recipePhotoPathList.size) {
////
////                            }
////                        }
//                        Log.d("zxc", "uploadImages: " + recipeImagePathList.size)
//                        continuation.resume(listOf(uri.toString()))
//                    }
//                    .addOnFailureListener {
//                        continuation.resumeWithException(it)
//                    }
//            }
//        }
    }

    //파일타입 가져오기
    private fun getFileExtension(context: Context, uri: Uri): String? {
        val cr: ContentResolver = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    override suspend fun update(
        request: UpdateRecipeRequest,
        onProgress: (Float) -> Unit
    ): RecipeData {
        val editData = HashMap<String, Any>()

        // FIXME: 2022-12-15 PhotoList 에 갱신된 데이터 넣는 것 수정하기
        for (i: Int in 0 until request.recipePhotoPathList.orEmpty().size) {
            if (!TextUtils.isEmpty(request.recipePhotoPathList.orEmpty()[i])) {
                editData["photoUrlList"] = request.recipePhotoPathList
            }
        }

        editData["desc"] = request.content

        return suspendCoroutine<RecipeData> { continuation ->
//            firebaseFirestore.collection("Recipe")
//                .document(recipeData.key.orEmpty())
//                .update(editData)
//                .addOnSuccessListener { continuation.resume(recipeData) }
//                .addOnFailureListener { continuation.resumeWithException(it) }
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