package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    private val getUserUseCase: GetUserUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    suspend fun getUserAccount() {
        getUserUseCase.invoke()
    }

    override fun onCleared() {
        super.onCleared()
    }
}