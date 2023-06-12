package com.hero.recipespace.view.main.recipe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.request.UpdateRecipeRequest
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import com.hero.recipespace.domain.recipe.usecase.UpdateRecipeUseCase
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditRecipeUiState {
    data class Success(val recipe: RecipeEntity) : EditRecipeUiState()

    data class Failed(val message: String) : EditRecipeUiState()

    object Idle : EditRecipeUiState()
}

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase
) : ViewModel() {

    private val _editRecipeUiState = MutableStateFlow<EditRecipeUiState>(EditRecipeUiState.Idle)
    val editRecipeUiState: StateFlow<EditRecipeUiState> = _editRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipeImageList = MutableLiveData<List<String>>()
    val recipeImageList: LiveData<List<String>>
        get() = _recipeImageList

    val oldRecipeImageList: MutableLiveData<List<String>> = MutableLiveData()
    val recipeContent: MutableLiveData<String> = MutableLiveData()

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    companion object {
        const val KEY_RECIPE_KEY = "key"
    }

    init {
        val recipeKey = savedStateHandle.get<String>(KEY_RECIPE_KEY).orEmpty()

        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess {
                    _recipe.value = it
                    oldRecipeImageList.value = it.photoUrlList.orEmpty()
                    recipeContent.value = it.desc.orEmpty()

                    addRecipePhotoList(it.photoUrlList.orEmpty())
                }
                .onFailure(WLog::e)
        }
    }

    fun updateRecipe(content: String) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            val key = recipe.value?.key ?: return@launch
            val recipePhotoPathList = _recipeImageList.value.orEmpty()

            updateRecipeUseCase(
                UpdateRecipeRequest(
                    key,
                    content,
                    recipePhotoPathList
                ),
                onProgress = { progress ->
                    _loadingState.value = LoadingState.Progress(progress.toInt())
                }
            )
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _editRecipeUiState.value = EditRecipeUiState.Success(it)
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _editRecipeUiState.value = EditRecipeUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    fun addRecipePhotoList(photoPathList: List<String>) {
        _recipeImageList.value = _recipeImageList.value.orEmpty() + photoPathList
    }

    fun deletePhoto(position: Int) {
        _recipeImageList.value = _recipeImageList.value.orEmpty()
            .toMutableList()
            .apply {
                removeAt(position)
            }
    }

    override fun onCleared() {
        super.onCleared()
    }
}