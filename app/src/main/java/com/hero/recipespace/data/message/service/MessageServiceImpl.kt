package com.hero.recipespace.data.message.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hero.recipespace.data.message.MessageData
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MessageServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : MessageService {

    override fun getData(messageKey: String): MessageData {
        TODO("Not yet implemented")
    }

    override suspend fun getDataList(chatKey: String): List<MessageData> {
        return suspendCoroutine { continuation ->
            db.collection("Chat")
                .document(chatKey)
                .collection("Messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        continuation.resumeWithException(e)
                        return@EventListener
                    }
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty) {
                        continuation.resumeWithException(Exception("queryDocumentSnapshot is Null or Empty"))
                        return@EventListener
                    }

                    val messageList = queryDocumentSnapshots.documentChanges.map {
                        it.document.toObject(MessageData::class.java)
                    }

                    continuation.resume(messageList)
                })
        }
    }



    override suspend fun add(chatKey: String, message: String) : MessageData {
        return suspendCoroutine<MessageData> { continuation ->
            val myUserKey: String = firebaseAuth.uid.orEmpty()
            val messageData = MessageData(myUserKey, message, Timestamp.now())

            db.runTransaction<Any> { transaction ->
                val chatRef = db.collection("Chat").document(chatKey)
                val messageRef = chatRef.collection("Messages").document()
                transaction.update(chatRef, "lastMessage", messageData)
                transaction[messageRef] = messageData
                continuation.resume(messageData)
                null
            }
        }
    }

    override suspend fun update(chatKey: String, message: String) : MessageData {
        TODO("Not yet implemented")
    }

    override suspend fun remove(chatKey: String, message: String) : MessageData {
        TODO("Not yet implemented")
    }
}