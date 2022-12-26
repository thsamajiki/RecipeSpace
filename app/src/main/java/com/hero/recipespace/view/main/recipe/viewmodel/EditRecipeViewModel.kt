package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
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
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase
) : AndroidViewModel(application) {

    private val _editRecipeUiState = MutableStateFlow<EditRecipeUiState>(EditRecipeUiState.Idle)
    val editRecipeUiState: StateFlow<EditRecipeUiState> = _editRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipeImageList = MutableLiveData<List<String>>()
    val recipeImageList: LiveData<List<String>>
        get() = _recipeImageList

    val recipeSelectedImageCount: MutableLiveData<String> = MutableLiveData()

    val recipeContent: MutableLiveData<String> = MutableLiveData()

//    private val _recipe = MutableLiveData<RecipeEntity>()
//    val recipe: LiveData<RecipeEntity>
//        get() = _recipe

    companion object {
        const val KEY_RECIPE_KEY = "key"
    }

    init {
        val recipeKey = savedStateHandle.get<String>(KEY_RECIPE_KEY).orEmpty()

        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess { recipe ->

                }
                .onFailure(WLog::e)
        }
    }

    val newRecipeContent: MutableLiveData<String> = MutableLiveData()

    fun updateRecipe(
        content: String,
        recipePhotoPathList: List<String>
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            updateRecipeUseCase(
                UpdateRecipeRequest(content, recipePhotoPathList),
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

    override fun onCleared() {
        super.onCleared()
    }
}