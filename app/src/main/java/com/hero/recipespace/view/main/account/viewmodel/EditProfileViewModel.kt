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

    val newUserName: MutableLiveData<String> = MutableLiveData()
    val newProfileImageUrl: MutableLiveData<String> = MutableLiveData()
//    val user: LiveData<UserEntity> = getUserUseCase().asLiveData()

    suspend fun requestUpdateProfile(userEntity: UserEntity) {
        updateUserUseCase.invoke(userEntity)
    }

    // TODO: 2022-12-14 EditProfileViewModel, UpdateUserUseCase, Repository, RemoteSource, LocalSource, UserService에서 update 메소드를 새로 만듦
    // todo: 새로운 이름과 프로필 이미지를 입력받기 위해서 만듦
    suspend fun requestUpdateProfile(newUserName: String = "", newProfileImageUrl: String = "") {
        updateUserUseCase.invoke(newUserName, newProfileImageUrl)
    }


    override fun onCleared() {
        super.onCleared()
    }
}