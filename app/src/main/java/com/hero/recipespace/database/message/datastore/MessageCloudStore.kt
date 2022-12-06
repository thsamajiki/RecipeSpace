package com.hero.recipespace.database.message.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class MessageCloudStore() {

    companion object {
        private lateinit var instance : MessageCloudStore

        fun getInstance() : MessageCloudStore {
            return synchronized(this) {
                instance ?: MessageCloudStore().also {
                    instance = it
                }
            }
        }
    }

    fun getData(messageKey: String) : Flow<MessageData> {

    }

    fun getDataList() : Flow<List<MessageData>> {
        TODO("Not yet implemented")
    }

    fun add(data: MessageData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<MessageData> {
            override fun apply(transaction: Transaction): MessageData {
                val messageRef: DocumentReference = fireStore.collection("Chat")
                    .document()

                data.key = messageRef.id


                transaction.set(messageRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<MessageData> {
            override fun onSuccess(messageData: MessageData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun update(data: MessageData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<MessageData> {
            override fun apply(transaction: Transaction): MessageData {
                val messageRef: DocumentReference = fireStore.collection("Chat")
                    .document()

                data.key = messageRef.id


                transaction.update(messageRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<MessageData> {
            override fun onSuccess(messageData: MessageData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun remove(data: MessageData) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<MessageData> {
            override fun apply(transaction: Transaction): MessageData {
                val messageRef: DocumentReference = fireStore.collection("Chat")
                    .document()

                data.key = messageRef.id


                transaction.delete(messageRef)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<MessageData> {
            override fun onSuccess(messageData: MessageData?) {

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }
}