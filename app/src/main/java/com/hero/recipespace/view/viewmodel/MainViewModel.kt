package com.hero.recipespace.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainUIState {

    data class Success(val userEntity: UserEntity) : MainUIState()

    data class Failed(val message: String) : MainUIState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : ViewModel() {

    private val _mainUiState = MutableLiveData<MainUIState>()
    val mainUiState: LiveData<MainUIState>
        get() = _mainUiState

    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity>
        get() = _user

    val profileImageUrl: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    init {
        getUser()

        viewModelScope.launch {
            getLoggedUserUseCase(
            )
                .onSuccess {
                    _mainUiState.value = MainUIState.Success(it)
                }
                .onFailure {
                    _mainUiState.value = MainUIState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            getUserUseCase(FirebaseAuth.getInstance().uid.orEmpty())
                .onSuccess {
                    _user.value = it
                    profileImageUrl.value = it.profileImageUrl.orEmpty()
                    userName.value = it.name.orEmpty()
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}