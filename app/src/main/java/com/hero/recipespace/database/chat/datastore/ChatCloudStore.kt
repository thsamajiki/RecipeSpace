package com.hero.recipespace.database.chat.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener
import java.lang.Exception

class ChatCloudStore(
    private val context: Context
) : CloudStore<ChatData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : ChatCloudStore

        fun getInstance(context: Context) : ChatCloudStore {
            return instance ?: synchronized(this) {
                instance ?: ChatCloudStore(context).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<ChatData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        key: String,
        onCompleteListener: OnCompleteListener<List<ChatData>>
    ): LiveData<List<ChatData>> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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
                onCompleteListener.onComplete(true, chatData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun update(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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
                onCompleteListener.onComplete(true, chatData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun remove(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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
                onCompleteListener.onComplete(true, chatData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

}