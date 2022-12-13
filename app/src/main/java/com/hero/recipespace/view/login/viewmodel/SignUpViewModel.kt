package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.usecase.AddUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application,
    private val addUserUseCase: AddUserUseCase
) : AndroidViewModel(application) {

    val email: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val pwd: MutableLiveData<String> = MutableLiveData()

    fun signUpUserAccount(userName: String,
                          email: String,
                          pwd: String) = viewModelScope.launch {
        addUserUseCase.invoke(userName, email, pwd)
    }

    override fun onCleared() {
        super.onCleared()
    }
}