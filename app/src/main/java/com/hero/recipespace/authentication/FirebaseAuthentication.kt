package com.hero.recipespace.authentication

import android.content.Context
import android.text.TextUtils
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.UserData
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil

class FirebaseAuthentication {
    private var firebaseAuthentication: FirebaseAuthentication? = null
    private var onCompleteListener: OnCompleteListener<Void>? = null

    companion object {
        private var instance: FirebaseAuthentication? = null

        fun getInstance(): FirebaseAuthentication {
            return instance ?: synchronized(this) {
                instance ?: FirebaseAuthentication().also {
                    instance = it
                }
            }
        }
    }

    fun setOnCompleteListener(onCompleteListener: OnCompleteListener<Void>) {
        this.onCompleteListener = onCompleteListener
    }

    fun signUpEmail(context: Context, email: String, pwd: String) {
        val response: Response<Void> = Response()
        response.setType(Type.AUTH)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd)
            .addOnSuccessListener {
                MyInfoUtil.getInstance().putEmail(context, email)
                MyInfoUtil.getInstance().putPwd(context, pwd)
                onCompleteListener?.onComplete(true, response)
            }.addOnFailureListener { onCompleteListener?.onComplete(false, response) }
    }

    fun login(context: Context, email: String, pwd: String?) {
        val response: Response<Void> = Response()
        response.setType(Type.AUTH)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email!!, pwd!!)
            .addOnSuccessListener {
                MyInfoUtil.getInstance().putEmail(context, email)
                MyInfoUtil.getInstance().putPwd(context, pwd)
                getUserInfo(context)
            }
            .addOnFailureListener { onCompleteListener?.onComplete(false, response) }
    }

    fun getUserInfo(context: Context) {
        val response: Response<Void> = Response()
        response.setType(Type.AUTH)
        val myKey: String = MyInfoUtil.getInstance().getKey()
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("User")
            .document(myKey)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData: UserData = documentSnapshot.toObject(UserData::class.java)
                    MyInfoUtil.getInstance().putUserName(context, userData.userName)
                    MyInfoUtil.getInstance().putProfileImageUrl(context, userData.profileImageUrl)
                    onCompleteListener?.onComplete(true, response)
                } else {
                    onCompleteListener?.onComplete(false, response)
                }
            }
            .addOnFailureListener { onCompleteListener?.onComplete(false, response) }
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun autoLogin(context: Context?) {
        val firebaseUser = getCurrentUser()
        val response: Response<Void> = Response()
        response.setType(Type.AUTH)
        if (firebaseUser == null) {
            onCompleteListener.onComplete(false, response)
        } else {
            val email: String = MyInfoUtil.getInstance().getEmail(context)
            val pwd: String = MyInfoUtil.getInstance().getPwd(context)
            if (TextUtils.isEmpty(email)) {
                onCompleteListener?.onComplete(false, response)
                return
            }
            val emailAuthCredential = EmailAuthProvider.getCredential(email, pwd)
            firebaseUser.reauthenticate(emailAuthCredential)
                .addOnSuccessListener { onCompleteListener?.onComplete(true, response) }
                .addOnFailureListener { onCompleteListener?.onComplete(false, response) }
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}