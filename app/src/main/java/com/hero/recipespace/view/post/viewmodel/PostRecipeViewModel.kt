package com.hero.recipespace.view.post.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.request.UploadRecipeRequest
import com.hero.recipespace.domain.recipe.usecase.PostRecipeUseCase
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PostRecipeUiState {
    data class Success(val recipe: RecipeEntity) : PostRecipeUiState()

    data class Failed(val message: String) : PostRecipeUiState()

    object Idle : PostRecipeUiState()
}

@HiltViewModel
class PostRecipeViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val postRecipeUseCase: PostRecipeUseCase
) : AndroidViewModel(application) {

    private val _postRecipeUiState = MutableStateFlow<PostRecipeUiState>(PostRecipeUiState.Idle)
    val postRecipeUiState: StateFlow<PostRecipeUiState> = _postRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipeImageList = MutableLiveData<List<String>>()
    val recipeImageList: LiveData<List<String>>
        get() = _recipeImageList

    val recipeSelectedImageCount: MutableLiveData<String> = MutableLiveData("0")

    val recipeContent: MutableLiveData<String> = MutableLiveData()

    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity>
        get() = _user

    fun uploadRecipe(
        content: String,
        recipePhotoPathList: List<String>
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            postRecipeUseCase(
                UploadRecipeRequest(content, recipePhotoPathList),
                onProgress = { progress ->
                    _loadingState.value = LoadingState.Progress(progress.toInt())
                }
            )
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _postRecipeUiState.value = PostRecipeUiState.Success(it)
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _postRecipeUiState.value = PostRecipeUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }

        viewModelScope.launch {
            getLoggedUserUseCase()
                .onSuccess {
                    _user.value = it
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun addRecipePhotoList(photoPathList: List<String>) {
        _recipeImageList.value = _recipeImageList.value.orEmpty() + photoPathList

        updateSelectedImageCount()
    }

    fun deletePhoto(position: Int) {
        _recipeImageList.value = _recipeImageList.value.orEmpty().toMutableList().apply {
            removeAt(position)
        }

        updateSelectedImageCount()
    }

    private fun updateSelectedImageCount() {
        recipeSelectedImageCount.value = (recipeImageList.value?.size ?: "0").toString()
    }
}