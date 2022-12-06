package com.hero.recipespace.database.user.datastore

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class UserCloudStore() {
    companion object {
        private lateinit var instance : UserCloudStore

        fun getInstance() : UserCloudStore {
            return synchronized(this) {
                instance ?: UserCloudStore().also {
                    instance = it
                }
            }
        }
    }

    fun getData(userKey: String) : Flow<UserData> {
        TODO("Not yet implemented")
    }

    fun getDataList() : Flow<List<UserData>> {
        TODO("Not yet implemented")
    }

    fun add(data: UserData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<UserData> {
            override fun apply(transaction: Transaction): UserData {
                val userRef: DocumentReference = fireStore.collection("User")
                    .document()

                data.key = userRef.id


                transaction.set(userRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<UserData> {
            override fun onSuccess(userData: UserData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun update(data: UserData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<UserData> {
            override fun apply(transaction: Transaction): UserData {
                val userRef: DocumentReference = fireStore.collection("User")
                    .document()

                data.key = userRef.id


                transaction.update(userRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<UserData> {
            override fun onSuccess(userData: UserData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun remove(data: UserData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<UserData> {
            override fun apply(transaction: Transaction): UserData {
                val userRef: DocumentReference = fireStore.collection("User")
                    .document()

                data.key = userRef.id


                transaction.delete(userRef)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<UserData> {
            override fun onSuccess(userData: UserData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }
}