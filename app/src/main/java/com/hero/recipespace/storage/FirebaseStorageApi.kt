package com.hero.recipespace.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.hero.recipespace.listener.OnFileUploadListener
import java.io.File

class FirebaseStorageApi {
    private var instance: FirebaseStorageApi? = null
    private var onFileUploadListener: OnFileUploadListener? = null

    companion object {
        val DEFAULT_IMAGE_PATH = "images/"
        val PROFILE_IMAGE_PATH = "profile/"

        private var instance: FirebaseStorageApi ?= null

        fun getInstance(): FirebaseStorageApi {
            return instance ?: synchronized(this) {
                instance ?: FirebaseStorageApi().also {
                    instance = it
                }
            }
        }
    }

    fun setOnFileUploadListener(onFileUploadListener: OnFileUploadListener?) {
        this.onFileUploadListener = onFileUploadListener
    }

    fun uploadImage(folderPath: String, filePath: String?) {
        val file = Uri.fromFile(File(filePath))
        val storageRef =
            FirebaseStorage.getInstance().reference.child(folderPath + file.lastPathSegment)
        val uploadTask = storageRef.putFile(file)
        uploadTask
            .addOnProgressListener { taskSnapshot ->
                val percent =
                    taskSnapshot.bytesTransferred.toFloat() / taskSnapshot.totalByteCount.toFloat() * 100
                onFileUploadListener?.onFileUploadProgress(percent)
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                storageRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                onFileUploadListener?.onFileUploadComplete(true,
                    uri.toString())
            }.addOnFailureListener { onFileUploadListener?.onFileUploadComplete(false, null) }
    }
}