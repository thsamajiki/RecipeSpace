package com.hero.recipespace.data.message.service

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.util.MyInfoUtil
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MessageServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : MessageService {
    override fun getData(messageKey: String): MessageData {
        return MessageData()
    }

    override suspend fun getDataList(chatKey: String): List<MessageData> {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("Chat")
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

                    val list = queryDocumentSnapshots.documentChanges.map {
                        it.document.toObject(MessageData::class.java)
                    }

                    continuation.resume(list)
                })
        }
    }



    override suspend fun add(chatKey: String, message: String) : MessageData {
        return suspendCoroutine<MessageData> { continuation ->
            val myUserKey: String = firebaseAuth.uid.orEmpty()
            val messageData = MessageData(myUserKey, message, Timestamp.now())

            firebaseFirestore.runTransaction<Any> { transaction ->
                val chatRef = firebaseFirestore.collection("Chat").document(chatKey)
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