package com.hero.recipespace.database.rate.datastore

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener
import java.lang.Exception

class RateCloudStore : CloudStore<RateData>() {

    companion object {
        private lateinit var instance: RateCloudStore

        fun getInstance(context: Context) : RateCloudStore {
            return instance ?: synchronized(this) {
                instance ?: RateCloudStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<RateData>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<RateData> {
            override fun apply(transaction: Transaction): RateData {
                val rateRef: DocumentReference = fireStore.collection("Rate")
                    .document()

                data.key = rateRef.id


                transaction.set(rateRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<RateData> {
            override fun onSuccess(rateData: RateData?) {
                onCompleteListener.onComplete(true, rateData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun update(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<RateData> {
            override fun apply(transaction: Transaction): RateData {
                val rateRef: DocumentReference = fireStore.collection("Rate")
                    .document()

                data.key = rateRef.id


                transaction.update(rateRef, data)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<RateData> {
            override fun onSuccess(rateData: RateData?) {
                onCompleteListener.onComplete(true, rateData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }

    override suspend fun remove(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.runTransaction(object : Transaction.Function<RateData> {
            override fun apply(transaction: Transaction): RateData {
                val rateRef: DocumentReference = fireStore.collection("Rate")
                    .document()

                data.key = rateRef.id


                transaction.delete(rateRef)

                return data
            }
        }).addOnSuccessListener(object : OnSuccessListener<RateData> {
            override fun onSuccess(rateData: RateData?) {
                onCompleteListener.onComplete(true, rateData)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                onCompleteListener.onComplete(false, null)
            }
        })
    }
}