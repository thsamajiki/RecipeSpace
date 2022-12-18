package com.hero.recipespace.view.login.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.domain.user.usecase.AddUserUseCase
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignUpUiState {
    object Success : SignUpUiState()

    data class Failed(val message: String) : SignUpUiState()

    object Idle : SignUpUiState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application,
    private val addUserUseCase: AddUserUseCase
) : AndroidViewModel(application) {

    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    val email: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val pwd: MutableLiveData<String> = MutableLiveData()

    fun signUpUserAccount(userName: String,
                          email: String,
                          pwd: String) {

        if (!checkEmailValid(email)) {
            _signUpUiState.value = SignUpUiState.Failed("이메일 양식을 확인해주세요")
            return
        }
        if (TextUtils.isEmpty(pwd)) {
            _signUpUiState.value = SignUpUiState.Failed("패스워드를 입력해주세요")
            return
        }
        if (TextUtils.isEmpty(userName)) {
            _signUpUiState.value = SignUpUiState.Failed("사용자명을 입력해주세요")
            return
        }

        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            addUserUseCase(SignUpUserRequest(userName, email, pwd))
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _signUpUiState.value = SignUpUiState.Failed("회원가입에 실패했습니다.")
                }
        }

    }

    private fun checkEmailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCleared() {
        super.onCleared()
    }
}