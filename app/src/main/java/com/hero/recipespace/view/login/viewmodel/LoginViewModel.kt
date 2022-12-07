package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
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

    private fun getMyAccount(email: String, password: String) = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

    private fun setLoginResult(isLogin: Boolean) {
        viewModelScope.launch {
            getUserUseCase.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}