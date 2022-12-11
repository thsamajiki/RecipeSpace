package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.usecase.AddUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application,
    private val addUserUseCase: AddUserUseCase
) : AndroidViewModel(application) {

    suspend fun signUpUserAccount(userName: String,
                                  email: String,
                                  pwd: String) = viewModelScope.launch {
        addUserUseCase.invoke(userName, email, pwd)
    }

    fun signUpEmail(context: Context, email: String, pwd: String) {
//        val response: Response<Void> = Response()
//        response.setType(Type.AUTH)
        var onResult: Boolean = false
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd)
//            .addOnSuccessListener {
//                MyInfoUtil.getInstance().putEmail(context, email)
//                MyInfoUtil.getInstance().putPwd(context, pwd)
//                onCompleteListener?.onComplete(true, response)
//            }.addOnFailureListener {
//                onCompleteListener?.onComplete(false, response)
//            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val registeredUser = UserData(
                        userKey = firebaseUser.uid,
                        userName = firebaseUser.displayName,
                        email = firebaseUser.email,
                        profileImageUrl = firebaseUser.photoUrl.toString()
                    )
                }

            }
    }

    override fun onCleared() {
        super.onCleared()
    }
}