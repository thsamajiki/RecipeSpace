package com.hero.recipespace.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.util.WLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUIState {

    data class Success(val userEntity: UserEntity): SplashUIState()

    data class Failed(val message: String): SplashUIState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getLoggedUserUseCase: GetLoggedUserUseCase
): ViewModel() {
    private val _splashUiState = MutableLiveData<SplashUIState>()
    val splashUiState: LiveData<SplashUIState>
        get() = _splashUiState

    init {
        viewModelScope.launch {
            delay(1000)
            getLoggedUserUseCase()
                .onSuccess {
                    _splashUiState.value = SplashUIState.Success(it)
                }
                .onFailure {
                    WLog.e(it)
                    _splashUiState.value = SplashUIState.Failed(it.message.orEmpty())
                }
        }
    }
}