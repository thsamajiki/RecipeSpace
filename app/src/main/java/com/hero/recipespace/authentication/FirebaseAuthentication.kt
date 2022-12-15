package com.hero.recipespace.authentication

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private var firebaseAuthentication: FirebaseAuthentication? = null

    companion object {
        private var instance: FirebaseAuthentication? = null

        fun getInstance(firebaseAuth: FirebaseAuth): FirebaseAuthentication {
            return synchronized(this) {
                instance ?: FirebaseAuthentication(firebaseAuth).also {
                    instance = it
                }
            }
        }
    }

    fun signUpEmail(context: Context, email: String, pwd: String, onResult: (Boolean) -> Unit) {
//        val response: Response<Void> = Response()
//        response.setType(Type.AUTH)

    }

    fun login(context: Context, email: String, pwd: String) {
//        val response: Response<Void> = Response()
//        response.setType(Type.AUTH)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
            .addOnSuccessListener {
//                MyInfoUtil.getInstance().putEmail(context, email)
//                MyInfoUtil.getInstance().putPwd(context, pwd)
//                getUserInfo(context)
            }
            .addOnFailureListener {
//                onCompleteListener?.onComplete(false, response)
            }
    }

    private fun getUserInfo(context: Context) {
        val response: Response<Void> = Response()
        response.setType(Type.AUTH)
        val myKey: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("User")
            .document(myKey)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData: UserData? = documentSnapshot.toObject(UserData::class.java)
//                    MyInfoUtil.getInstance().putUserName(context, userData.userName)
//                    MyInfoUtil.getInstance().putProfileImageUrl(context, userData.profileImageUrl)
//                    onCompleteListener?.onComplete(true, response)
                } else {
//                    onCompleteListener?.onComplete(false, response)
                }
            }
            .addOnFailureListener {
//                onCompleteListener?.onComplete(false, response)
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

//    fun autoLogin(context: Context?) {
//        val firebaseUser = getCurrentUser()
//        if (firebaseUser == null) {
//            onCompleteListener.onComplete(false, response)
//        } else {
//            val email: String = MyInfoUtil.getInstance().getEmail(context)
//            val pwd: String = MyInfoUtil.getInstance().getPwd(context)
//            if (TextUtils.isEmpty(email)) {
//                onCompleteListener?.onComplete(false, response)
//                return
//            }
//            val emailAuthCredential = EmailAuthProvider.getCredential(email, pwd)
//            firebaseUser.reauthenticate(emailAuthCredential)
//                .addOnSuccessListener { onCompleteListener?.onComplete(true, response) }
//                .addOnFailureListener { onCompleteListener?.onComplete(false, response) }
//        }
//    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}