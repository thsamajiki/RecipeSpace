package com.hero.recipespace.storage

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseStorageApi {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    companion object {
        private var instance: FirebaseStorageApi? = null

        fun getInstance(): FirebaseStorageApi {
            return synchronized(this) {
                instance ?: FirebaseStorageApi().also {
                    instance = it
                }
            }
        }
    }
}
