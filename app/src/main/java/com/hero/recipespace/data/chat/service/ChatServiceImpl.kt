package com.hero.recipespace.data.chat.service

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.message.service.MessageService
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.service.UserService
import com.hero.recipespace.domain.chat.request.AddChatRequest
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.main.chat.MessageType
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ChatServiceImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val userService: UserService,
    private val messageService: MessageService,
) : ChatService {
    override suspend fun getData(chatKey: String): ChatData {
        return suspendCoroutine { continuation ->
            db.collection("Chat")
                .document(chatKey)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot == null) {
                        return@addOnSuccessListener
                    }

                    val chatData: ChatData? = documentSnapshot.toObject(ChatData::class.java)

                    WLog.d("chatData $chatData")

                    if (chatData != null) {
                        continuation.resume(chatData)
                    } else {
                        continuation.resumeWithException(Exception("ChatData is null"))
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun getChatByRecipeChatInfo(
        myKey: String,
        recipeChatInfo: RecipeChatInfo,
    ): ChatData {
        return suspendCoroutine { continuation ->
            val userList =
                listOf(
                    myKey,
                    recipeChatInfo.userKey,
                )

            db.collection("Chat")
                .whereArrayContainsAny(
                    "userList",
                    userList,
                )
                .whereEqualTo(
                    "recipeKey",
                    recipeChatInfo.recipeKey,
                )
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val chatData =
                        queryDocumentSnapshots.documents.firstNotNullOfOrNull {
                            it.toObject(ChatData::class.java)
                        }

                    WLog.d("chatData $chatData")
                    if (chatData != null) {
                        continuation.resume(chatData)
                    } else {
                        continuation.resumeWithException(Exception("chat data is empty"))
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun getDataList(userKey: String): List<ChatData> {
        return suspendCoroutine { continuation ->
            val userList = listOf(userKey)

            db.collection("Chat")
                .whereArrayContainsAny(
                    "userList",
                    userList,
                )
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val chatList =
                        queryDocumentSnapshots.documentChanges.map {
                            it.document.toObject(ChatData::class.java)
                        }
                    WLog.d("chatList $chatList")

                    continuation.resume(chatList)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun observeNewChat(userKey: String): Flow<Pair<DocumentChange.Type, ChatData>> {
        return callbackFlow {
            db.collection("Chat")
                .whereArrayContainsAny(
                    "userList",
                    listOf(userKey),
                )
                .addSnapshotListener(
                    EventListener { queryDocumentSnapshots, e ->
                        if (e != null) {
                            return@EventListener
                        }
                        if (queryDocumentSnapshots != null) {
                            for (documentChange in queryDocumentSnapshots.documentChanges) {
                                val chatData: ChatData =
                                    documentChange.document.toObject(ChatData::class.java)

                                CoroutineScope(Dispatchers.IO).launch {
                                    val messageCount =
                                        messageService.getMessageCount(chatData.key, userKey)
                                    chatData.unreadMessageCount = messageCount
                                    this@callbackFlow.trySend(documentChange.type to chatData)
                                }
                            }
                        }
                    },
                )
            awaitClose {
            }
        }
    }

    override suspend fun add(request: AddChatRequest): ChatData {
        val myUserData = userService.getUserData(auth.uid.orEmpty())

        return suspendCoroutine { continuation ->
            db.runTransaction(
                Transaction.Function<Any> { transaction ->
                    val myUserKey: String = myUserData.key
                    val myProfileUrl: String = myUserData.profileImageUrl.orEmpty()
                    val myUserName: String = myUserData.name.orEmpty()

                    val userRef =
                        db.collection("User")
                            .document(request.otherUserKey)
                    val userData: UserData =
                        transaction[userRef].toObject(UserData::class.java)
                            ?: return@Function null
                    transaction[userRef] = userData

                    val userProfileImages = HashMap<String, String>()
                    userProfileImages[myUserKey] = myProfileUrl
                    userProfileImages[userData.key] = userData.profileImageUrl.orEmpty()

                    val userNames = HashMap<String, String>()
                    userNames[myUserKey] = myUserName
                    userNames[userData.key] = userData.name.orEmpty()

                    val userList =
                        listOf(
                            myUserKey,
                            userData.key,
                        )

                    val chatRef =
                        db.collection("Chat")
                            .document()
                    val chatKey = chatRef.id

                    val lastMessage =
                        MessageData(
                            chatKey = chatKey,
                            userKey = myUserKey,
                            message = request.message,
                            timestamp = Timestamp.now(),
                            isRead = false,
                            messageType = MessageType.MESSAGE,
                        )

                    val chatData =
                        ChatData(
                            key = chatKey,
                            lastMessage = lastMessage,
                            userProfileImages = userProfileImages,
                            userNames = userNames,
                            userList = userList,
                            recipeKey = request.recipeKey,
                        )
                    transaction[chatRef] = chatData

                    val messageRef =
                        chatRef.collection("Messages")
                            .document()
                    transaction[messageRef] = lastMessage

                    chatData
                },
            )
                .addOnSuccessListener { chatData ->
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
            val myUserKey: String = auth.uid.orEmpty()
            db.collection("Chat")
                .whereArrayContains(
                    "userList",
                    listOf(
                        otherUserKey,
                        myUserKey,
                    ),
                )
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val chatData =
                        queryDocumentSnapshots.documents.firstOrNull()
                            ?.toObject(ChatData::class.java)
                    continuation.resume(chatData != null)
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}
