package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.domain.user.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : AndroidViewModel(application) {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

//    val user: LiveData<UserEntity> = getUserUseCase().asLiveData()

    suspend fun requestUpdateProfile(userEntity: UserEntity) {
        updateUserUseCase.invoke(userEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}