package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}