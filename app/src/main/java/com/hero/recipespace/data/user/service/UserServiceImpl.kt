package com.hero.recipespace.data.user.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
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

    override suspend fun add(userData: UserData) : UserData {
        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.userKey.orEmpty())
                .set(userData)
                .addOnSuccessListener {
                    MyInfoUtil.getInstance().putUserName(context, userData.userName)
                    continuation.resume(userData)
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun update(userData: UserData) : UserData {
        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.userKey.orEmpty())
                .update(editData)
                .addOnSuccessListener { continuation.resume(userData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun remove(userData: UserData) : UserData {
        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.userKey.orEmpty())
                .delete()
                .addOnSuccessListener { continuation.resume(userData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}