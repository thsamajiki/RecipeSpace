package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.util.MyInfoUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase,
) : AndroidViewModel(application) {

    val email: MutableLiveData<String> = MutableLiveData()
    val pwd: MutableLiveData<String> = MutableLiveData()

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
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setLoginResult(getCurrentUser()?.uid.orEmpty())
                } else {

                }
                MyInfoUtil.getInstance().putEmail(context, email)
                MyInfoUtil.getInstance().putPwd(context, pwd)
                getUserUseCase.invoke()

            }
    }

    private fun setLoginResult(userKey: String) {
        viewModelScope.launch {
            getUserUseCase.invoke(userKey)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun onCleared() {
        super.onCleared()
    }
}