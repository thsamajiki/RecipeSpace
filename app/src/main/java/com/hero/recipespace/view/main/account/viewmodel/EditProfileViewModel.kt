package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}