package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.usecase.LoginUserUseCase
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    data class Success(val user: UserEntity) : LoginUiState()

    data class Failed(val message: String) : LoginUiState()

    object Idle : LoginUiState()
}

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    application: Application,
    private val loginUserUseCase: LoginUserUseCase,
) : AndroidViewModel(application) {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    val email: MutableLiveData<String> = MutableLiveData()
    val pwd: MutableLiveData<String> = MutableLiveData()

    fun requestLogin(
        email: String,
        pwd: String,
    ) {
        if (!checkEmailValid(email)) {
            _loginUiState.value = LoginUiState.Failed("이메일 양식을 확인해주세요")
            return
        }
        if (pwd.isEmpty()) {
            _loginUiState.value = LoginUiState.Failed("비밀번호를 입력해주세요")
            return
        }

        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            loginUserUseCase.invoke(
                LoginUserRequest(
                    email,
                    pwd,
                ),
            )
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _loginUiState.value = LoginUiState.Success(it)
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _loginUiState.value = LoginUiState.Failed("로그인에 실패했습니다. 다시 시도해주세요")
                    it.printStackTrace()
                }
        }
    }

    private fun checkEmailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
