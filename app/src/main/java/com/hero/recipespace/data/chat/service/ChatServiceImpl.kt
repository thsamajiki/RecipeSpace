package com.hero.recipespace.data.chat.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil
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

    override suspend fun add(chatData: ChatData) : ChatData {
        return suspendCoroutine<ChatData> { continuation ->
            firebaseFirestore.runTransaction(Transaction.Function<Any> { transaction ->
                val myUserKey: String = firebaseAuth.uid.orEmpty()
                val myProfileUrl: String = firebaseAuth.currentUser?.photoUrl.toString().orEmpty()
                val myUserName: String = firebaseAuth.currentUser?.displayName.orEmpty()
                val userRef = firebaseFirestore
                    .collection("User").document(otherUserKey)
                val userData: UserData = transaction[userRef].toObject(UserData::class.java)
                    ?: return@Function null
                transaction[userRef] = userData
                val userProfiles = HashMap<String, String>()
                userProfiles[myUserKey] = myProfileUrl
                userProfiles[userData.userKey] = userData.profileImageUrl
                val userNames = HashMap<String, String>()
                userNames[myUserKey] = myUserName
                userNames[userData.userKey] = userData.userName
                val userList = HashMap<String, Boolean>()
                userList[myUserKey] = true
                userList[userData.userKey] = true
                val lastMessage = MessageData(
                    userKey = ,
                    message = ,
                    timestamp = Timestamp.now()
                )
                lastMessage.setMessage(message)
                lastMessage.setUserKey(myUserKey)
                lastMessage.setTimestamp(Timestamp.now())
                val chatRef = firebaseFirestore.collection("Chat").document()
                val chatData = ChatData(userProfiles = userProfiles,
                        userNames = userNames,
                        userList = userList,
                        lastMessage = lastMessage,
                        key = chatRef.id)
//                chatData.userProfiles = userProfiles
//                chatData.userNames = userNames
//                chatData.userList = userList
//                chatData.lastMessage = lastMessage
//                chatData.key = chatRef.id
                transaction[chatRef] = chatData
                val messageRef = chatRef.collection("Messages").document()
                transaction[messageRef] = lastMessage
                chatData
            }).addOnSuccessListener { chatData ->
                continuation.resume(chatData)
            }.addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun update(chatData: ChatData) {

    }

    override suspend fun remove(chatData: ChatData) {

    }
}