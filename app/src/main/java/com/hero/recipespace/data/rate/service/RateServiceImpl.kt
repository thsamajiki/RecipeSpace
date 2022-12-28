package com.hero.recipespace.data.rate.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.request.AddRateRequest
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RateServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : RateService {

    override suspend fun getData(rateKey: String, recipeKey: String): RateData {
        return suspendCoroutine { continuation ->
            db.collection("Recipe")
                .document(recipeKey)
                .collection("RateList")
                .document(rateKey)
                .get()
                .addOnSuccessListener { documentSnapShot ->
                    if (documentSnapShot == null) {
                        return@addOnSuccessListener
                    }

                    val rateData = documentSnapShot.toObject(RateData::class.java)

                    continuation.resume(rateData!!)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override fun getDataList(): List<RateData> {
        TODO("Not yet implemented")
    }

//    override suspend fun add(recipeKey: String) : RateData {
//        return suspendCoroutine<RateData> { continuation ->
//            firebaseFirestore.runTransaction(Transaction.Function<Any?> { transaction ->
//                val recipeRef = firebaseFirestore.collection("Recipe").document(recipeKey)
//                val rateRef = recipeRef.collection("RateList").document(rateData.userKey.orEmpty())
//                val rateSnapShot = transaction[rateRef]
//                val originTotalCount: Int = recipeData.totalRatingCount
//                val originRate: Float = recipeData.rate
//                var originSum = originTotalCount * originRate
//                var newTotalCount = originTotalCount + 1
//                if (rateSnapShot.exists()) {
//                    val myOriginRateData: RateData ?= rateSnapShot.toObject(RateData::class.java)
//                    val myOriginRate: Float = myOriginRateData.rate
//                    originSum -= myOriginRate
//                    newTotalCount--
//                }
//                val userRate: Float = rateData.rate
//                val newRate = (originSum + userRate) / newTotalCount
//                recipeData.totalRatingCount = newTotalCount
//                recipeData.rate = newRate
//                transaction[rateRef] = rateData
//                transaction[recipeRef] = recipeData
//                recipeData
//            }).addOnSuccessListener { data ->
//                continuation.resume(rateData)
//            }.addOnFailureListener { continuation.resumeWithException(it) }
//        }
//    }
//
//    override suspend fun add(rate: Float, recipeKey: String) : RateData {
//        return suspendCoroutine<RateData> { continuation ->
//            firebaseFirestore.runTransaction(Transaction.Function<Any?> { transaction ->
//                val recipeRef = firebaseFirestore.collection("Recipe").document(recipeKey)
//                val rateRef = recipeRef.collection("RateList").document(rateData.userKey.orEmpty())
//                val rateSnapShot = transaction[rateRef]
//                val originTotalCount: Int = recipeData.totalRatingCount
//                val originRate: Float = recipeData.rate
//                var originSum = originTotalCount * originRate
//                var newTotalCount = originTotalCount + 1
//                if (rateSnapShot.exists()) {
//                    val myOriginRateData: RateData ?= rateSnapShot.toObject(RateData::class.java)
//                    val myOriginRate: Float = myOriginRateData?.rate!!.toFloat()
//                    originSum -= myOriginRate
//                    newTotalCount--
//                }
//                val userRate: Float = rateData.rate
//                val newRate = (originSum + userRate) / newTotalCount
//                recipeData.totalRatingCount = newTotalCount
//                recipeData.rate = newRate
//                transaction[rateRef] = rateData
//                transaction[recipeRef] = recipeData
//                recipeData
//            }).addOnSuccessListener { data ->
//                continuation.resume(rateData)
//            }.addOnFailureListener { continuation.resumeWithException(it) }
//        }
//    }

    override suspend fun add(request: AddRateRequest, recipeData: RecipeData): RateData {
        return suspendCoroutine<RateData> { continuation ->
            db.runTransaction(Transaction.Function<Any?> { transaction ->
                val recipeRef = db.collection("Recipe").document(recipeData.key)
                val rateRef = recipeRef.collection("RateList").document(request.userKey)
                val rateSnapshot = transaction[rateRef]
                val originTotalCount: Int = recipeData.totalRatingCount ?: 0
                val originRate: Float = recipeData.rate ?: 0f
                var originSum = originTotalCount * originRate
                var newTotalCount = originTotalCount + 1

                if (rateSnapshot.exists()) {
                    val myOriginRateData: RateData ?= rateSnapshot.toObject(RateData::class.java)
                    val myOriginRate: Float = myOriginRateData?.rate!!.toFloat()
                    originSum -= myOriginRate
                    newTotalCount--
                }

                val userRate: Float = request.rate
                val newRate = (originSum + userRate) / newTotalCount

                val rateData = RateData(
                    key = recipeRef.collection("RateList").document(request.userKey).id,
                    userKey = request.userKey,
                    rate = request.rate,
                    date = Timestamp.now()
                )

                val newRecipeData = RecipeData(
                    key = recipeData.key,
                    profileImageUrl = recipeData.profileImageUrl,
                    userName = recipeData.userName,
                    userKey = recipeData.userKey,
                    desc = recipeData.desc,
                    photoUrlList = recipeData.photoUrlList,
                    postDate = recipeData.postDate,
                    rate = newRate,
                    totalRatingCount = newTotalCount
                )

                transaction[rateRef] = rateData
                transaction[recipeRef] = newRecipeData

                rateData
            }).addOnSuccessListener { rateData ->
                continuation.resume(rateData as RateData)
            }.addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun update(request: UpdateRateRequest, recipeData: RecipeData) : RateData {
        return suspendCoroutine<RateData> { continuation ->
            db.runTransaction(Transaction.Function<Any?> { transaction ->

                val recipeRef = db.collection("Recipe").document(recipeData.key)
                val rateRef = recipeRef.collection("RateList").document(request.userKey)
                val rateSnapShot = transaction[rateRef]
                val originTotalCount: Int = recipeData.totalRatingCount ?: 0
                val originRate: Float = recipeData.rate ?: 0f
                var originSum = originTotalCount * originRate

                if (rateSnapShot.exists()) {
                    val myOriginRateData: RateData ?= rateSnapShot.toObject(RateData::class.java)
                    val myOriginRate: Float = myOriginRateData?.rate!!.toFloat()
                    originSum -= myOriginRate
                }

                val userRate: Float = request.rate
                val newRate = (originSum + userRate) / originTotalCount

                val editRateData = HashMap<String, Any>()
                editRateData["rate"] = newRate
                transaction.update(rateRef, editRateData)

                val editRecipeData = HashMap<String, Any>()
                editRecipeData["rate"] = newRate
                transaction.update(recipeRef, editRecipeData)

                val newRateData = RateData(
                    key = request.key,
                    userKey = request.userKey,
                    rate = newRate,
                    date = Timestamp.now()
                )

                newRateData
            })
                .addOnSuccessListener { rateData ->
                    continuation.resume(rateData as RateData)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun remove(rateKey: String) : RateData {
        TODO("Not yet implemented")
    }
}