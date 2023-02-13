package com.hero.recipespace.view.main.account.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.domain.user.usecase.UpdateUserUseCase
import com.hero.recipespace.util.WLog
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
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : AndroidViewModel(application) {

    private val _editProfileUiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val editProfileUiState: StateFlow<EditProfileUiState> = _editProfileUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _profileImage: MutableLiveData<String> = MutableLiveData()
    val profileImage: LiveData<String>
        get() = _profileImage

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    val userName: String?
        get() = user.value?.name

    val userKey: String = FirebaseAuth.getInstance().uid.orEmpty()

    val newUserName: MutableLiveData<String> = MutableLiveData()
    var newProfileImagePath: String? = null
        private set

    fun setNewProfileImagePath(path: String) {
        newProfileImagePath = path
        _profileImage.value = path
    }

    init {
        viewModelScope.launch {
            getLoggedUserUseCase()
                .onSuccess {
                    _user.value = it
                    _profileImage.value = it.profileImageUrl.orEmpty()
                }
                .onFailure(WLog::e)
        }
    }

    fun requestUpdateProfile() {
        if (!isNewProfile()) return

        val newProfileImageUrl = newProfileImagePath ?: return
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

    private fun isNewProfile(): Boolean {
        val oldProfileImageUrl = user.value?.profileImageUrl
        val newProfileImagePath = newProfileImagePath

        return when {
            oldProfileImageUrl.isNullOrEmpty() -> !newProfileImagePath.isNullOrEmpty()
            newProfileImagePath.isNullOrEmpty() -> false
            else -> oldProfileImageUrl != newProfileImagePath
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}