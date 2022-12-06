package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase
) : AndroidViewModel(application) {

    val user: LiveData<UserEntity> = getUserUseCase().asLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}