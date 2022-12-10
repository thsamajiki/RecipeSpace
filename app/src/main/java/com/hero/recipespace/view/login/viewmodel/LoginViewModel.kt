package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.listener.Response
import com.hero.recipespace.listener.Type
import com.hero.recipespace.util.MyInfoUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase
) : AndroidViewModel(application) {

    fun requestLogin() {
        viewModelScope.launch {
            getMyAccount()
        }
    }

    private fun getMyAccount(email: String, password: String) =
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setLoginResult(true)
            }.addOnFailureListener {

            }

    fun login2(context: Context, email: String, pwd: String?) {
        val response: Response<Void> = Response()
        response.setType(Type.AUTH)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd!!)
            .addOnSuccessListener {
                MyInfoUtil.getInstance().putEmail(context, email)
                MyInfoUtil.getInstance().putPwd(context, pwd)
                getUserInfo(context)
            }
            .addOnFailureListener { onCompleteListener?.onComplete(false, response) }
    }

    private fun setLoginResult(isLogin: Boolean) {
        viewModelScope.launch {
            getUserUseCase.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}