package com.hero.recipespace.data.chat.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.user.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ChatServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ChatService {
    override suspend fun getData(chatKey: String): ChatData {
        TODO("Not yet implemented")
    }

    override suspend fun getChatByUserKeys(myKey: String, otherUserKey: String) : ChatData {
        return suspendCoroutine { continuation ->
            val myUserKey: String = firebaseAuth.uid.orEmpty()
            val userList: MutableList<String> = ArrayList()
            userList.add(myUserKey)
            userList.add(otherUserKey)
            firebaseFirestore.collection("Chat")
                .whereEqualTo("userList.$otherUserKey", true)
                .whereEqualTo("userList.$myUserKey", true)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val chatData = queryDocumentSnapshots.documents
                        .firstOrNull()
                        ?.toObject(ChatData::class.java)

                    continuation.resume(requireNotNull(chatData))
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun getDataList(userKey: String): List<ChatData> {
        return suspendCoroutine { continuation ->
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("Chat")
                .whereEqualTo("userList.$userKey", true)
                .addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        return@EventListener
                    }
                    if (queryDocumentSnapshots != null) {
                        for (documentChange in queryDocumentSnapshots.documentChanges) {
                            val chatData: ChatData =
                                documentChange.document.toObject(ChatData::class.java)
                            onChatListChangeListener.onChatListChange(documentChange.type, chatData)
                        }
                    }
                })
        }
    }

    override suspend fun observeNewChat(userKey: String): Flow<Pair<DocumentChange.Type, ChatData>> {
        return callbackFlow {
            firebaseFirestore.collection("Chat")
                .whereEqualTo("userList.$userKey", true)
                .addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        return@EventListener
                    }
                    if (queryDocumentSnapshots != null) {
                        for (documentChange in queryDocumentSnapshots.documentChanges) {
                            val chatData: ChatData =
                                documentChange.document.toObject(ChatData::class.java)

                            this.trySend(documentChange.type to chatData)
                        }
                    }
                })
        }
    }

    override suspend fun add(otherUserKey: String,
                             message: String) : ChatData {
        return suspendCoroutine { continuation ->
            firebaseFirestore.runTransaction(Transaction.Function<Any> { transaction ->
                val myUserKey: String = firebaseAuth.uid.orEmpty()
                val myProfileUrl: String = firebaseAuth.currentUser?.photoUrl?.toString().orEmpty()
                val myUserName: String = firebaseAuth.currentUser?.displayName.orEmpty()
                val userRef = firebaseFirestore.collection("User").document(
                    otherUserKey)
                val userData: UserData = transaction[userRef].toObject(UserData::class.java)
                    ?: return@Function null
                transaction[userRef] = userData
                val userProfiles = HashMap<String, String>()
                userProfiles[myUserKey] = myProfileUrl
                userProfiles[userData.key.orEmpty()] = userData.profileImageUrl.orEmpty()
                val userNames = HashMap<String, String>()
                userNames[myUserKey] = myUserName
                userNames[userData.key.orEmpty()] = userData.name.orEmpty()
                val userList = HashMap<String, Boolean>()
                userList[myUserKey] = true
                userList[userData.key.orEmpty()] = true
                val lastMessage = MessageData(
                    userKey = myUserKey,
                    message = message,
                    timestamp = Timestamp.now()
                )
                val chatRef = firebaseFirestore.collection("Chat").document()
                val chatData = ChatData(
                    key = chatRef.id,
                    lastMessage = lastMessage,
                    userProfileImages = userProfiles,
                    userNames = userNames,
                    userList = userList
                )
                transaction[chatRef] = chatData
                val messageRef = chatRef.collection("Messages").document()
                transaction[messageRef] = lastMessage
                chatData
            }).addOnSuccessListener { chatData ->
                continuation.resume(chatData as ChatData)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun update(chatData: ChatData) {

    }

    override suspend fun remove(chatData: ChatData) {

    }

    override suspend fun checkExistChatData(otherUserKey: String): Boolean {
        return suspendCoroutine { continuation ->
            val myUserKey: String = firebaseAuth.uid.orEmpty()
            val userList: MutableList<String> = ArrayList()
            userList.add(myUserKey)
            userList.add(otherUserKey)
            firebaseFirestore.collection("Chat")
                .whereEqualTo("userList.$otherUserKey", true)
                .whereEqualTo("userList.$myUserKey", true)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val chatData = queryDocumentSnapshots.documents.firstOrNull()?.toObject(ChatData::class.java)
                    continuation.resume(chatData != null)

                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}