package com.hero.recipespace.database

import android.content.Context
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.hero.recipespace.data.*
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.*
import com.hero.recipespace.util.MyInfoUtil

class FirebaseData {

    companion object {
        private var firebaseData: FirebaseData? = null

        fun getInstance(): FirebaseData {
            return synchronized(this) {
                firebaseData ?: FirebaseData().also {
                    firebaseData = it
                }
            }
        }
    }

    fun uploadUserData(
        context: Context,
        userData: UserData,
        onCompleteListener: OnCompleteListener<Void>,
    ) {
        val response: Response<Void> = Response()
        response.setType(Type.FIRE_STORE)
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("User")
            .document(userData.userKey)
            .set(userData)
            .addOnSuccessListener {
                MyInfoUtil.getInstance().putUserName(context, userData.userName)
                onCompleteListener.onComplete(true, response)
            }
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun uploadRecipeData(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val documentReference = firestore.collection("RecipeData").document()
        val key = documentReference.id
        recipeData.key = key
        val response: Response<RecipeData> = Response()
        response.setType(Type.FIRE_STORE)
        response.setData(recipeData)
        documentReference.set(recipeData)
            .addOnSuccessListener { onCompleteListener.onComplete(true, response) }
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun modifyRecipeData(
        recipeKey: String,
        editData: HashMap<String, Any>,
        onCompleteListener: OnCompleteListener<Void>,
    ) {
        val response: Response<Void> = Response()
        response.setType(Type.FIRE_STORE)
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("RecipeData")
            .document(recipeKey)
            .update(editData)
            .addOnSuccessListener { onCompleteListener.onComplete(true, response) }
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun deleteRecipeData(
        recipeKey: String,
        editData: HashMap<String, Any>,
        onCompleteListener: OnCompleteListener<Void>,
    ) {
        val response: Response<Void> = Response()
        response.setType(Type.FIRE_STORE)
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("RecipeData")
            .document(recipeKey)
            .delete()
            .addOnSuccessListener { onCompleteListener.onComplete(true, response) }
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun downloadRecipeData(onCompleteListener: OnCompleteListener<List<RecipeData>>) {
        val response: Response<List<RecipeData>> = Response()
        response.setType(Type.FIRE_STORE)
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("RecipeData")
            .orderBy("postDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(OnSuccessListener { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.isEmpty) {
                    return@OnSuccessListener
                }
                val recipeDataList: List<RecipeData> = mutableListOf()
                for (documentSnapshot in queryDocumentSnapshots.documents) {
                    val recipeData: RecipeData = documentSnapshot.toObject(RecipeData::class.java)
                    recipeDataList.add(recipeData)
                }
                response.setData(recipeDataList)
                onCompleteListener.onComplete(true, response)
            })
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun uploadRating(
        recipeData: RecipeData,
        rateData: RateData,
        onCompleteListener: OnCompleteListener<RecipeData>
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val response: Response<RecipeData> = Response()
        response.setType(Type.FIRE_STORE)
        firestore.runTransaction(Transaction.Function<Any?> { transaction ->
            val recipeRef = firestore.collection("RecipeData").document(recipeData.key)
            val rateRef = recipeRef.collection("RateList").document(rateData.userKey)
            val rateSnapShot = transaction[rateRef]
            val originTotalCount: Int = recipeData.totalRatingCount
            val originRate: Float = recipeData.rate
            var originSum = originTotalCount * originRate
            var newTotalCount = originTotalCount + 1
            if (rateSnapShot.exists()) {
                val myOriginRateData: RateData ?= rateSnapShot.toObject(RateData::class.java)
                val myOriginRate: Float = myOriginRateData.rate
                originSum -= myOriginRate
                newTotalCount--
            }
            val userRate: Float = rateData.rate
            val newRate = (originSum + userRate) / newTotalCount
            recipeData.totalRatingCount = newTotalCount
            recipeData.rate = newRate
            transaction[rateRef] = rateData
            transaction[recipeRef] = recipeData
            recipeData
        }).addOnSuccessListener { data ->
            response.date = date
            onCompleteListener.onComplete(true, response)
        }.addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun updateUserData(
        userKey: String,
        editData: HashMap<String, Any>,
        onCompleteListener: OnCompleteListener<Void>,
    ) {
        val response: Response<Void> = Response()
        response.setType(Type.FIRE_STORE)
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("User")
            .document(userKey)
            .update(editData)
            .addOnSuccessListener { onCompleteListener.onComplete(true, response) }
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun getChatList(
        userKey: String,
        onChatListChangeListener: OnChatListChangeListener
    ): ListenerRegistration {
        val firestore = FirebaseFirestore.getInstance()
        return firestore.collection("Chat")
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

    fun createChatRoom(
        context: Context,
        otherUserKey: String,
        message: String,
        onCompleteListener: OnCompleteListener<ChatData>
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val response: Response<ChatData> = Response()
        response.setType(Type.FIRE_STORE)
        firestore.runTransaction(Transaction.Function<Any> { transaction ->
            val myUserKey: String = MyInfoUtil.getInstance().getKey()
            val myProfileUrl: String = MyInfoUtil.getInstance().getProfileImageUrl(context)
            val myUserName: String = MyInfoUtil.getInstance().getUserName(context)
            val userRef = firestore.collection("User").document(
                otherUserKey)
            val userData: UserData = transaction[userRef].toObject(UserData::class.java)
                ?: return@Function null
            transaction[userRef] = userData
            val userProfiles = HashMap<String, String>()
            userProfiles[myUserKey] = myProfileUrl
            userProfiles[userData.userKey] = userData.profileImageUrl
            val userNames = HashMap<String, String>()
            userNames[myUserKey] = myUserName
            userNames[userData.getUserKey()] = userData.userName
            val userList = HashMap<String, Boolean>()
            userList[myUserKey] = true
            userList[userData.userKey] = true
            val lastMessage = MessageData()
            lastMessage.setMessage(message)
            lastMessage.setUserKey(myUserKey)
            lastMessage.setTimestamp(Timestamp.now())
            val chatRef = firestore.collection("Chat").document()
            val chatData = ChatData()
            chatData.userProfiles = userProfiles
            chatData.userNames = userNames
            chatData.userList = userList
            chatData.lastMessage = lastMessage
            chatData.key = chatRef.id
            transaction[chatRef] = chatData
            val messageRef = chatRef.collection("Messages").document()
            transaction[messageRef] = lastMessage
            chatData
        }).addOnSuccessListener { chatData ->
            response.setData(chatData)
            onCompleteListener.onComplete(true, response)
        }.addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun getMessageList(
        chatDataKey: String?,
        onMessageListener: OnMessageListener,
    ): ListenerRegistration? {
        val firestore = FirebaseFirestore.getInstance()
        return firestore.collection("Chat")
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

    fun sendMessage(message: String, chatData: ChatData) {
        val messageData = MessageData()
        val myUserKey: String = MyInfoUtil.getInstance().getKey()
        messageData.userKey = myUserKey
        messageData.setMessage(message)
        messageData.setTimestamp(Timestamp.now())
        val firestore = FirebaseFirestore.getInstance()
        firestore.runTransaction<Any> { transaction ->
            val chatRef = firestore.collection("Chat").document(chatData.key)
            val messageRef = chatRef.collection("Messages").document()
            transaction.update(chatRef, "lastMessage", messageData)
            transaction[messageRef] = messageData
            null
        }
    }

    fun checkExistChatData(
        otherUserKey: String,
        onCompleteListener: OnCompleteListener<ChatData>,
    ) {
        val myUserKey: String? = MyInfoUtil.getInstance().getKey()
        val userList: MutableList<String> = ArrayList()
        userList.add(myUserKey)
        userList.add(otherUserKey)
        val firestore = FirebaseFirestore.getInstance()
        val response: Response<ChatData> = Response()
        response.setType(Type.FIRE_STORE)
        firestore.collection("Chat")
            .whereEqualTo("userList.$otherUserKey", true)
            .whereEqualTo("userList.$myUserKey", true)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val documentSnapshotList = queryDocumentSnapshots.documents
                    if (documentSnapshotList.size > 0) {
                        val chatData: ChatData =
                            documentSnapshotList[0].toObject(ChatData::class.java)
                        response.setData(chatData)
                    }
                }
                onCompleteListener.onComplete(true, response)
            }
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }

    fun getNoticeList(onCompleteListener: OnCompleteListener<List<NoticeData>>): Task<QuerySnapshot> {
        val response: Response<ArrayList<NoticeData>> = Response()
        response.setType(Type.FIRE_STORE)
        val firestore = FirebaseFirestore.getInstance()
        return firestore.collection("Notice")
            .orderBy("postDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(OnSuccessListener { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.isEmpty) {
                    return@OnSuccessListener
                }
                val noticeDataList: List<NoticeData> = mutableListOf()
                for (documentSnapshot in queryDocumentSnapshots.documents) {
                    val noticeData: NoticeData = documentSnapshot.toObject(NoticeData::class.java)
                    noticeDataList.add(noticeData)
                }
                response.setData(noticeDataList)
                onCompleteListener.onComplete(true, response)
            })
            .addOnFailureListener { onCompleteListener.onComplete(false, response) }
    }
}