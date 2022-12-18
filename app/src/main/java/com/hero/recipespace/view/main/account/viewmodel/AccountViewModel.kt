package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.domain.user.usecase.SignOutUseCase
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignOutUiState {
    object Success : SignOutUiState()

    data class Failed(val message: String) : SignOutUiState()

    object Idle : SignOutUiState()
}

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : AndroidViewModel(application) {

    private val _signOutUiState = MutableStateFlow<SignOutUiState>(SignOutUiState.Idle)
    val signOutUiState: StateFlow<SignOutUiState> = _signOutUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    val profileImageUrl: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
//    val user: LiveData<UserEntity> = getUserUseCase().asLiveData()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            getUserUseCase()
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

    fun signOut() {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            signOutUseCase()
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _signOutUiState.value = SignOutUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    fun refreshUserProfile() {
        getUser()
    }

    override fun onCleared() {
        super.onCleared()
    }
}