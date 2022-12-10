package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.user.usecase.AddUserUseCase
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application,
    private val addUserUseCase: AddUserUseCase
) : AndroidViewModel(application) {

    suspend fun signUpUserAccount() {
        addUserUseCase.invoke()
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

    override fun onCleared() {
        super.onCleared()
    }
}