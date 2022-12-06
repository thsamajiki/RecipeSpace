package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    application: Application,
    private val updateUserUseCase: UpdateUserUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    fun getUserAccount() {

    }

    suspend fun updateUserAccount(userEntity: UserEntity) {
        updateUserUseCase.invoke(userEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}