package com.hero.recipespace.data.user.service

import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.user.UserData
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : UserService {
    override fun getData(userKey: String): UserData {
        TODO("Not yet implemented")
    }

    override fun getFirebaseAuthProfile(): UserData {
        TODO("Not yet implemented")
    }

    override fun getDataList(): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(userName: String, email: String, pwd: String): UserData {
        if (createAccount(email, pwd)) {
            val userData = UserData(
                key = firebaseAuth.uid,
                name = userName,
                email = email,
                profileImageUrl = firebaseAuth.currentUser?.photoUrl?.toString().orEmpty()
            )

            return suspendCoroutine<UserData> { continuation ->
                firebaseFirestore.collection("User")
                    .document(userData.key.orEmpty())
                    .set(userData)
                    .addOnSuccessListener {
                        continuation.resume(userData)
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        }
    }

    private suspend fun createAccount(email: String, pwd: String): Boolean {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener {
                    firebaseAuth.currentUser?.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .build()
                    )
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun update(userData: UserData) : UserData {
        val editData = HashMap<String, Any>()

        if (!TextUtils.isEmpty(userData.profileImageUrl)) {
            editData["profileImageUrl"] = userData.profileImageUrl as String
        }
        editData["name"] = userData.name as String

        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.key.orEmpty())
                .update(editData)
                .addOnSuccessListener { continuation.resume(userData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

//    override suspend fun update(newUserName: String, newProfileImageUrl: String): UserData {
//        val userData = UserData(
//            key = firebaseAuth.uid,
//            name = firebaseAuth.currentUser?.displayName,
//            email = firebaseAuth.currentUser?.email,
//            profileImageUrl = firebaseAuth.currentUser?.photoUrl.toString()
//        )
//
//        val newUserData = UserData(
//            key = firebaseAuth.uid,
//            name = newUserName,
//            email = firebaseAuth.currentUser?.email,
//            profileImageUrl = newProfileImageUrl
//        )
//
//        return suspendCoroutine<UserData> { continuation ->
//            firebaseFirestore.collection("User")
//                .document(userData.key.orEmpty())
//                .update(newUserData)
//                .addOnSuccessListener { continuation.resume(userData) }
//                .addOnFailureListener { continuation.resumeWithException(Throwable.) }
//        }
//    }

    // TODO EditProfileActivity를 통해 나의 사용자 정보(나의 프로필 사진, 나의 사용자 이름)가 변경되었을 때
    //  update() 메소드에서 트랜잭션 처리할 때 필요한 데이터
    //  1. UserData 의 profileImageUrl, name
    //  2. RecipeData 의 profileImageUrl, userName
    //  3. ChatActivity의 profileImageUrl, name
    private fun updateUserWithRecipeAndChat(userData: UserData, recipeData: RecipeData, chatData: ChatData) {
        firebaseFirestore.runTransaction { transaction ->
            val editUserData = HashMap<String, Any>()

            // 1. UserData 의 profileImageUrl, name
            if (!TextUtils.isEmpty(userData.profileImageUrl)) {
                editUserData["profileImageUrl"] = userData.profileImageUrl as String
            }
            editUserData["name"] = userData.name as String
            val userRef: DocumentReference = firebaseFirestore.collection("User")
                .document(userData.key.orEmpty())
            transaction.update(userRef, editUserData)


            // 2. RecipeData 의 profileImageUrl, name
            val editRecipeData = HashMap<String, Any>()
            if (!TextUtils.isEmpty(recipeData.profileImageUrl)) {
                editRecipeData["profileImageUrl"] = recipeData.profileImageUrl as String
            }
            editRecipeData["name"] = recipeData.userName as String
            val recipeRef: DocumentReference = firebaseFirestore.collection("Recipe")
                .document(recipeData.key.orEmpty())
            transaction.update(recipeRef, editRecipeData)

            // 3. ChatData 의 profileImageUrl, name
//            val editChatData = HashMap<String, Any>()
//            if (!TextUtils.isEmpty(chatData.profileImageUrl)) {
//                editChatData["profileImageUrl"] = chatData.profileImageUrl as String
//            }
//            editChatData["name"] = chatData.userName as String
//            val chatRef: DocumentReference = firebaseFirestore.collection("Chat")
//                .document(chatData.key.orEmpty())
//            transaction.update(chatRef, editChatData)

            null
        }
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    override suspend fun remove(userData: UserData) : UserData {
        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.key.orEmpty())
                .delete()
                .addOnSuccessListener { continuation.resume(userData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}