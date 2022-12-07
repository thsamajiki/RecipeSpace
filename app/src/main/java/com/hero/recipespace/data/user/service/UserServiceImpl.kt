package com.hero.recipespace.data.user.service

import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl @Inject constructor() : UserService {
    override fun getData(userKey: String): UserData {
        TODO("Not yet implemented")
    }

    override fun getFirebaseAuthProfile(): UserData {
        TODO("Not yet implemented")
    }

    override fun getDataList(): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(userData: UserData) {
        suspendCoroutine<UserData> {
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
    }

    override suspend fun update(userData: UserData) {
        suspendCoroutine<UserData> {
            val response: Response<Void> = Response()
            response.setType(Type.FIRE_STORE)
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("User")
                .document(userKey)
                .update(editData)
                .addOnSuccessListener { it. }
                .addOnFailureListener { onCompleteListener.onComplete(false, response) }
        }
    }

    override suspend fun remove(userData: UserData) {
        suspendCoroutine<UserData> {
            val response: Response<Void> = Response()
            response.setType(Type.FIRE_STORE)
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("User")
                .document(userData.userKey!!)
                .delete()
                .addOnSuccessListener { onCompleteListener.onComplete(true, response) }
                .addOnFailureListener { onCompleteListener.onComplete(false, response) }
        }
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

}