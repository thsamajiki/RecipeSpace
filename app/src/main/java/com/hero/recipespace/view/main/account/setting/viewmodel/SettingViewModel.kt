package com.hero.recipespace.view.main.account.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.domain.user.usecase.RemoveUserUseCase
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DropOutUiState {
    object Success : DropOutUiState()

    data class Failed(val message: String) : DropOutUiState()

    object Idle : DropOutUiState()
}

@HiltViewModel
class SettingViewModel
@Inject
constructor(
    private val removeUserUseCase: RemoveUserUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
) : ViewModel() {
    private val _dropOutUiState = MutableStateFlow<DropOutUiState>(DropOutUiState.Idle)
    val dropOutUiState: StateFlow<DropOutUiState> = _dropOutUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    init {
        viewModelScope.launch {
            getLoggedUserUseCase()
                .onSuccess {
                    _user.value = it
                }
                .onFailure(WLog::e)
        }
    }

    fun dropOut(userEntity: UserEntity) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            removeUserUseCase.invoke(userEntity)
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _dropOutUiState.value = DropOutUiState.Success
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden

                    when (it) {
                        is FirebaseNoSignedInUserException -> {
                            DropOutUiState.Failed("로그인되지 않은 사용자입니다.")
                        }

                        is FirebaseAuthInvalidUserException -> {
                            DropOutUiState.Failed("유효하지 않은 사용자입니다.")
                        }
                    }
                }
        }
    }
}
