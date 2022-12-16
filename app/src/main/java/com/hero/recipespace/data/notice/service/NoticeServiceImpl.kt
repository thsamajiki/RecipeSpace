package com.hero.recipespace.data.notice.service

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hero.recipespace.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NoticeServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : NoticeService {
    override suspend fun getData(noticeKey: String): NoticeData {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("Notice")
                .document(noticeKey)
                .get()
                .addOnSuccessListener { documentSnapShot ->
                    if (documentSnapShot == null) {
                        return@addOnSuccessListener
                    }
                    val noticeData = documentSnapShot.data?.getValue(noticeKey)

                    continuation.resume(noticeData as NoticeData)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getDataList(): Flow<List<NoticeData>> {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("Notice")
                .orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(OnSuccessListener { queryDocumentSnapshots ->
                    if (queryDocumentSnapshots.isEmpty) {
                        return@OnSuccessListener
                    }
                    val noticeDataList = queryDocumentSnapshots.documents
                        .mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(NoticeData::class.java)
                        }.asFlow()

                    continuation.resume(noticeDataList)
                })
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun add(noticeData: NoticeData) {
    }

    override suspend fun update(noticeData: NoticeData) {
    }

    override suspend fun remove(noticeData: NoticeData) {
    }
}