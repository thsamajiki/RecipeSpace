package com.hero.recipespace.data.rate.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.util.WLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RateServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : RateService {

    override suspend fun getData(userKey: String, recipeKey: String): RateData {
        return suspendCoroutine { continuation ->
            db.collection("Recipe")
                .document(recipeKey)
                .collection("RateList")
                .document(userKey)
                .get()
                .addOnSuccessListener { documentSnapShot ->
                    if (documentSnapShot == null) {
                        return@addOnSuccessListener
                    }

                    val rateData = documentSnapShot.toObject(RateData::class.java)

                    WLog.e("RateServiceImpl - rateData : ${rateData?.rateKey}")

                    if (rateData != null) {
                        continuation.resume(rateData)
                    } else {
                        continuation.resumeWithException(Exception("not found rateData"))
                    }

                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override fun getDataList(): List<RateData> {
        TODO("Not yet implemented")
    }

    private suspend fun add(request: UpdateRateRequest, recipeData: RecipeData): RateData {
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
                    val myOriginRateData: RateData? = rateSnapshot.toObject(RateData::class.java)
                    val myOriginRate: Float = myOriginRateData?.rate!!.toFloat()
                    originSum -= myOriginRate
                    newTotalCount--
                }

                val userRate: Float = request.rate
                val newRate = (originSum + userRate) / newTotalCount

                val rateData = RateData(
                    rateKey = request.userKey,
                    rate = request.rate,
                    date = Timestamp.now()
                )

                val newRecipeData = recipeData.copy(
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

    override suspend fun update(request: UpdateRateRequest, recipeData: RecipeData): RateData {
        WLog.d("request $request recipeData $recipeData")
        return suspendCoroutine { continuation ->
            db.runTransaction { transaction ->
                val recipeRef = db.collection("Recipe").document(recipeData.key)
                val rateRef = recipeRef.collection("RateList").document(request.userKey)

                val editRateData = HashMap<String, Any>()
                editRateData["rate"] = request.rate
                transaction.update(rateRef, editRateData)

                val newRateData = RateData(
                    rateKey = request.userKey,
                    rate = request.rate,
                    date = Timestamp.now()
                )

                newRateData
            }
                .addOnSuccessListener { rateData ->
                    continuation.resume(rateData as RateData)
                }
                .addOnFailureListener { exception ->
                    // 업데이트를 했는데 에러 발생.
                    if (exception is FirebaseFirestoreException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            kotlin.runCatching { add(request, recipeData) }
                                .onSuccess {
                                    continuation.resume(it)
                                }
                                .onFailure {
                                    continuation.resumeWithException(it)
                                }
                        }
                    } else {
                        continuation.resumeWithException(exception)
                    }
                }
        }
    }

    override suspend fun remove(rateKey: String): RateData {
        TODO("Not yet implemented")
    }
}