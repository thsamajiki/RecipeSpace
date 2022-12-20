package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.domain.user.usecase.UpdateUserUseCase
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditProfileUiState {
    data class Success(val user: UserEntity) : EditProfileUiState()

    data class Failed(val message: String) : EditProfileUiState()

    object Idle : EditProfileUiState()
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : AndroidViewModel(application) {

    private val _editProfileUiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val editProfileUiState: StateFlow<EditProfileUiState> = _editProfileUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

//    private val _user = MutableLiveData<UserEntity>()
//    val user: LiveData<UserEntity>
//        get() = _user

    companion object {
        const val USER_KEY = "user"
    }

//    val user: UserEntity = savedStateHandle.get<UserEntity>(USER_KEY)!!

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    val userKey: String = FirebaseAuth.getInstance().uid.orEmpty()

    val newUserName: MutableLiveData<String> = MutableLiveData()
    val newProfileImageUrl: MutableLiveData<String> = MutableLiveData()
//    val user: LiveData<UserEntity> = getUserUseCase().asLiveData()


    // TODO: 2022-12-14 EditProfileViewModel, UpdateUserUseCase, Repository, RemoteSource, LocalSource, UserService에서 update 메소드를 새로 만듦
    // todo: 새로운 이름과 프로필 이미지를 입력받기 위해서 만듦
    fun requestUpdateProfile(newProfileImageUrl: String = "") {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            updateUserUseCase(
                UpdateUserRequest(newUserName.value.orEmpty(), newProfileImageUrl)
            )
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _editProfileUiState.value = EditProfileUiState.Success(it)
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _editProfileUiState.value = EditProfileUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}