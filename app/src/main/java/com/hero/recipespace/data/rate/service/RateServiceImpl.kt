package com.hero.recipespace.data.rate.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.rate.RateData
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RateServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : RateService {
    override suspend fun getData(rateKey: String, recipeKey: String): RateData {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("Recipe")
                .document(recipeKey)
                .collection("RateList")
                .document(rateKey)
                .get()
                .addOnSuccessListener { documentSnapShot ->
                    if (documentSnapShot == null) {
                        return@addOnSuccessListener
                    }
                    val rateData = documentSnapShot.data?.getValue(rateKey)

                    continuation.resume(rateData as RateData)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override fun getDataList(): List<RateData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(recipeKey: String) : RateData {
        return suspendCoroutine<RateData> { continuation ->
            firebaseFirestore.runTransaction(Transaction.Function<Any?> { transaction ->
                val recipeRef = firebaseFirestore.collection("Recipe").document(recipeKey)
                val rateRef = recipeRef.collection("RateList").document(rateData.userKey.orEmpty())
                val rateSnapShot = transaction[rateRef]
                val originTotalCount: Int = recipeData.totalRatingCount
                val originRate: Float = recipeData.rate
                var originSum = originTotalCount * originRate
                var newTotalCount = originTotalCount + 1
                if (rateSnapShot.exists()) {
                    val myOriginRateData: RateData ?= rateSnapShot.toObject(RateData::class.java)
                    val myOriginRate: Float = myOriginRateData.rate
                    originSum -= myOriginRate
                    newTotalCount--
                }
                val userRate: Float = rateData.rate
                val newRate = (originSum + userRate) / newTotalCount
                recipeData.totalRatingCount = newTotalCount
                recipeData.rate = newRate
                transaction[rateRef] = rateData
                transaction[recipeRef] = recipeData
                recipeData
            }).addOnSuccessListener { data ->
                continuation.resume(rateData)
            }.addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun add(rate: Float, recipeKey: String) : RateData {
        return suspendCoroutine<RateData> { continuation ->
            firebaseFirestore.runTransaction(Transaction.Function<Any?> { transaction ->
                val recipeRef = firebaseFirestore.collection("Recipe").document(recipeKey)
                val rateRef = recipeRef.collection("RateList").document(rateData.userKey.orEmpty())
                val rateSnapShot = transaction[rateRef]
                val originTotalCount: Int = recipeData.totalRatingCount
                val originRate: Float = recipeData.rate
                var originSum = originTotalCount * originRate
                var newTotalCount = originTotalCount + 1
                if (rateSnapShot.exists()) {
                    val myOriginRateData: RateData ?= rateSnapShot.toObject(RateData::class.java)
                    val myOriginRate: Float = myOriginRateData?.rate!!.toFloat()
                    originSum -= myOriginRate
                    newTotalCount--
                }
                val userRate: Float = rateData.rate
                val newRate = (originSum + userRate) / newTotalCount
                recipeData.totalRatingCount = newTotalCount
                recipeData.rate = newRate
                transaction[rateRef] = rateData
                transaction[recipeRef] = recipeData
                recipeData
            }).addOnSuccessListener { data ->
                continuation.resume(rateData)
            }.addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun update(rateKey: String) : RateData {
        TODO("Not yet implemented")
    }

    override suspend fun remove(rateKey: String) : RateData {
        TODO("Not yet implemented")
    }
}