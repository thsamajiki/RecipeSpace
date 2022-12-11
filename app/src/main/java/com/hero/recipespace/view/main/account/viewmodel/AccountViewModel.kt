package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase
) : AndroidViewModel(application) {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

//    val user: LiveData<UserEntity> = getUserUseCase().asLiveData()

    fun getUser() {
        viewModelScope.launch {

        }
    }

    init {
        viewModelScope.launch {
            getUserUseCase.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}