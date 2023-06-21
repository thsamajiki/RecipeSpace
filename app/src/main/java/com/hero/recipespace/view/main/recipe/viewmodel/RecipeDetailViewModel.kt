package com.hero.recipespace.view.main.recipe.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.DeleteRecipeUseCase
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecipeDetailUIState {
    data class Success(val recipeEntity: RecipeEntity) : RecipeDetailUIState()

    data class Failed(val message: String) : RecipeDetailUIState()
}

sealed class DeleteRecipeUiState {
    object Success : DeleteRecipeUiState()

    data class Failed(val message: String) : DeleteRecipeUiState()

    object Idle : DeleteRecipeUiState()
}

sealed class IntentChatUIState {
    object Success : IntentChatUIState()

    data class Failed(val message: String) : IntentChatUIState()
}

sealed class OpenRateUIState {
    object Success : OpenRateUIState()

    data class Failed(val message: String) : OpenRateUIState()
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    companion object {
        const val RECIPE_KEY = "key"
    }

    private val _recipeDetailUiState = MutableLiveData<RecipeDetailUIState>()
    val recipeDetailUiState: LiveData<RecipeDetailUIState>
        get() = _recipeDetailUiState

    private val _deleteRecipeUiState = MutableStateFlow<DeleteRecipeUiState>(DeleteRecipeUiState.Idle)
    val deleteRecipeUiState: StateFlow<DeleteRecipeUiState> = _deleteRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    val profileImageUrl: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    val postDate: MutableLiveData<Timestamp?> = MutableLiveData()
    val desc: MutableLiveData<String> = MutableLiveData()

    val rate: MutableLiveData<Float?> = MutableLiveData()
    val photoUrlList: MutableLiveData<List<String>> = MutableLiveData()

    val recipeKey: String = savedStateHandle.get<String>(RECIPE_KEY)!!

    init {
        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess {
                    _recipe.value = it
                    profileImageUrl.value = it.profileImageUrl.orEmpty()
                    userName.value = it.userName
                    postDate.value = it.postDate
                    desc.value = it.desc.orEmpty()
                    rate.value = it.rate
                    photoUrlList.value = it.photoUrlList.orEmpty()
                }
                .onFailure {
                    it.printStackTrace()
                    Log.e("RecipeDetailViewModel", "$it ")
                }
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            deleteRecipeUseCase.invoke(recipe)
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _deleteRecipeUiState.value = DeleteRecipeUiState.Success
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _deleteRecipeUiState.value = DeleteRecipeUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}