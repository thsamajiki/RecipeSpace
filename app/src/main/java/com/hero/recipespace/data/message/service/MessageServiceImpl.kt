package com.hero.recipespace.data.message.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.util.WLog
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : MessageService {

    override fun getData(messageKey: String): MessageData {
        TODO("Not yet implemented")
    }

    override suspend fun getDataList(chatKey: String): Flow<List<MessageData>> {
        return callbackFlow {
            db.collection("Chat")
                .document(chatKey)
                .collection("Messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        throw e
                    }
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty) {
                        throw Exception("queryDocumentSnapshot is Null or Empty")
                    }

                    val messageList = queryDocumentSnapshots.documentChanges.mapNotNull {
                        it.document.toObject(MessageData::class.java).apply {
                            messageId = it.document.id
                        }
                    }

                    WLog.d("messageList $messageList")
                    trySend(messageList)
                }

            awaitClose {

            }
        }
    }


    override suspend fun add(chatKey: String, message: String): MessageData {
        return suspendCoroutine<MessageData> { continuation ->
            val myUserKey: String = firebaseAuth.uid.orEmpty()
            val messageData = MessageData(
                chatKey = chatKey,
                userKey = myUserKey,
                message = message,
                timestamp = Timestamp.now()
            )

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

    override suspend fun update(chatKey: String, message: String): MessageData {
        TODO("Not yet implemented")
    }

    override suspend fun remove(chatKey: String, message: String): MessageData {
        TODO("Not yet implemented")
    }
}