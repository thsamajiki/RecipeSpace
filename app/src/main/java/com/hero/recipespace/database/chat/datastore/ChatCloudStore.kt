package com.hero.recipespace.database.chat.datastore

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.chat.ChatData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class ChatCloudStore() {

    companion object {
        private var instance : ChatCloudStore? = null

        fun getInstance() : ChatCloudStore {
            return synchronized(this) {
                instance ?: ChatCloudStore().also {
                    instance = it
                }
            }
        }
    }

    fun getData(chatKey: String) : Flow<ChatData> {
        return callbackFlow {

        }
    }

    fun getDataList(
        key: String
    ): Flow<List<ChatData>> {
        return callbackFlow {

        }
    }

    fun add(data: ChatData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<ChatData> {
            override fun apply(transaction: Transaction): ChatData {
                val chatRef: DocumentReference = fireStore.collection("Chat").document()

                data.key = chatRef.id


                transaction.set(chatRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<ChatData> {
            override fun onSuccess(chatData: ChatData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun update(data: ChatData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<ChatData> {
            override fun apply(transaction: Transaction): ChatData? {
                val chatRef: DocumentReference = fireStore.collection("Chat").document()

                data.key = chatRef.id


                transaction.update(chatRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<ChatData> {
            override fun onSuccess(chatData: ChatData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun remove(data: ChatData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<ChatData> {
            override fun apply(transaction: Transaction): ChatData? {
                val chatRef: DocumentReference = fireStore.collection("Chat").document()

                data.key = chatRef.id


                transaction.delete(chatRef)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<ChatData> {
            override fun onSuccess(chatData: ChatData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

}