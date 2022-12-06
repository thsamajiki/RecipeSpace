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
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class RateCloudStore() {

    companion object {
        private lateinit var instance: RateCloudStore

        fun getInstance() : RateCloudStore {
            return synchronized(this) {
                instance ?: RateCloudStore().also {
                    instance = it
                }
            }
        }
    }

    fun getData(rateKey: String) : Flow<RateData> {
        TODO("Not yet implemented")
    }

    fun getDataList() : Flow<List<RateData>> {
        TODO("Not yet implemented")
    }

    fun add(data: RateData) {
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

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun update(data: RateData) {
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

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }

    fun remove(data: RateData) {
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

            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {

            }
        })
    }
}