package com.hero.recipespace.data.notice.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hero.recipespace.data.notice.NoticeData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NoticeServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : NoticeService {

    override suspend fun getData(noticeKey: String): NoticeData {
        return suspendCoroutine { continuation ->
            db.collection("Notice")
                .document(noticeKey)
                .get()
                .addOnSuccessListener { documentSnapShot ->
                    if (documentSnapShot == null) {
                        return@addOnSuccessListener
                    }
                    val noticeData = documentSnapShot.toObject(NoticeData::class.java)

                    continuation.resume(noticeData!!)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override fun getDataList(): Flow<List<NoticeData>> {
        return callbackFlow {
            db.collection("Notice")
                .orderBy("postDate", Query.Direction.DESCENDING)
                .addSnapshotListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        throw e
                    }
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty) {
                        throw Exception("queryDocumentSnapshot is Null or Empty")
                    }

                    val noticeDataList = queryDocumentSnapshots.documentChanges.mapNotNull {
                        it.document.toObject(NoticeData::class.java).apply {
                            key = it.document.id
                        }
                    }

                    trySend(noticeDataList)
                }

            awaitClose {

            }
        }
    }

    override suspend fun add(noticeData: NoticeData) {
    }

    override suspend fun update(noticeData: NoticeData) {
    }

    override suspend fun remove(noticeData: NoticeData) {
    }
}