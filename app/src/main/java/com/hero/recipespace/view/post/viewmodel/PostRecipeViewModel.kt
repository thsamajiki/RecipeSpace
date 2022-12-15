package com.hero.recipespace.view.post.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.request.UploadRecipeRequest
import com.hero.recipespace.domain.recipe.usecase.AddRecipeUseCase
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
    private val addRecipeUseCase: AddRecipeUseCase
) : AndroidViewModel(application) {

    private val _postRecipeUiState = MutableStateFlow<PostRecipeUiState>(PostRecipeUiState.Idle)
    val postRecipeUiState: StateFlow<PostRecipeUiState> = _postRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    val recipeContent: MutableLiveData<String> = MutableLiveData()

    fun uploadRecipe(
        content: String,
        recipePhotoPathList: List<String>
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            addRecipeUseCase(
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
    }
}