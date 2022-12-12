package com.hero.recipespace.data.message.service

import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.util.MyInfoUtil
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageServiceImpl @Inject constructor() : MessageService {
    override fun getData(messageKey: String): MessageData {
    }

    override fun getDataList(chatKey: String): List<MessageData> {
        return suspendCoroutine { continuation ->
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("Chat")
                .document(chatDataKey!!)
                .collection("Messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        onMessageListener.onMessage(false, null)
                        return@EventListener
                    }
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty) {
                        onMessageListener.onMessage(true, null)
                        return@EventListener
                    }
                    for (documentChange in queryDocumentSnapshots.documentChanges) {
                        val messageData: MessageData =
                            documentChange.document.toObject(MessageData::class.java)
                        onMessageListener.onMessage(true, messageData)
                    }
                })
        }
    }

    override suspend fun add(chatKey: String, message: String) : MessageData {
        return suspendCoroutine<MessageData> { continuation ->
            val myUserKey: String = MyInfoUtil.getInstance().getKey()
            val messageData = MessageData(myUserKey, message, Timestamp.now())

            val fireStore = FirebaseFirestore.getInstance()
            fireStore.runTransaction<Any> { transaction ->
                val chatRef = fireStore.collection("Chat").document(chatKey)
                val messageRef = chatRef.collection("Messages").document()
                transaction.update(chatRef, "lastMessage", messageData)
                transaction[messageRef] = messageData
                continuation.resume(messageData)
                null
            }
        }
    }

    override suspend fun update(messageData: MessageData) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(messageData: MessageData) {
        TODO("Not yet implemented")
    }
}