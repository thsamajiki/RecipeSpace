package com.hero.recipespace.data.user.service

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.authentication.FirebaseAuthentication
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil
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
                userKey = firebaseAuth.uid,
                userName = userName,
                email = email,
                profileImageUrl = firebaseAuth.currentUser?.photoUrl?.toString().orEmpty()
            )

            return suspendCoroutine<UserData> { continuation ->
                firebaseFirestore.collection("User")
                    .document(userData.userKey.orEmpty())
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