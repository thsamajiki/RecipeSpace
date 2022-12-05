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
import java.lang.Exception

class MessageCloudStore(
    private val context: Context
) : CloudStore<MessageData>(context, FirebaseFirestore.getInstance()) {

    companion object {
        private lateinit var instance : MessageCloudStore

        fun getInstance(context: Context) : MessageCloudStore {
            return instance ?: synchronized(this) {
                instance ?: MessageCloudStore(context).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<MessageData>) : LiveData<MessageData> {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<MessageData>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
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
                onCompleteListener.onComplete(true, messageData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun update(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
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
                onCompleteListener.onComplete(true, messageData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun remove(data: MessageData, onCompleteListener: OnCompleteListener<MessageData>) {
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
                onCompleteListener.onComplete(true, messageData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }
}