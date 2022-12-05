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
import java.lang.Exception

class UserCloudStore(
    private val context: Context
) : CloudStore<UserData>(context, FirebaseFirestore.getInstance()) {
    companion object {
        private lateinit var instance : UserCloudStore

        fun getInstance(context: Context) : UserCloudStore {
            return instance ?: synchronized(this) {
                instance ?: UserCloudStore(context).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<UserData>>,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
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
                onCompleteListener.onComplete(true, userData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun update(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
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
                onCompleteListener.onComplete(true, userData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun remove(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
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
                onCompleteListener.onComplete(true, userData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }
}