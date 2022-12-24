package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecipeDetailUIState {

    data class Success(val recipeEntity: RecipeEntity) : RecipeDetailUIState()

    data class Failed(val message: String) : RecipeDetailUIState()
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase
) : AndroidViewModel(application) {

    companion object {
        const val RECIPE_KEY = "key"
        const val RECIPE_USER_KEY = "userKey"
    }

    private val _recipeDetailUiState = MutableLiveData<RecipeDetailUIState>()
    val recipeDetailUiState: LiveData<RecipeDetailUIState>
        get() = _recipeDetailUiState

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    val recipeKey: String = savedStateHandle.get<String>(RECIPE_KEY)!!

    val recipeEntity: RecipeEntity = savedStateHandle.get<RecipeEntity>(RECIPE_KEY)!!

    init {
        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess {
                    _recipe.value = it
                    Log.d("zxc", "RecipeDetailViewModel: $recipeKey")
                    Log.d("zxc", "RecipeDetailViewModel: ${recipe.value!!.key}")
                    Log.d("zxc", "RecipeDetailViewModel: ${recipe.value!!.desc}")
                    Log.d("zxc", "RecipeDetailViewModel: ${recipe.value!!.userKey}")
                    Log.d("zxc", "RecipeDetailViewModel: ${recipe.value!!.userName}")
                }
                .onFailure {
                    it.printStackTrace()
                    Log.e("RecipeDetailViewModel", "$it ")
                }
        }

        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess {
                    _recipeDetailUiState.value = RecipeDetailUIState.Success(it)
                }
                .onFailure {
                    _recipeDetailUiState.value = RecipeDetailUIState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }



//    val chatKey: String = savedStateHandle.get<String>(RECIPE_USER_KEY)!!

    override fun onCleared() {
        super.onCleared()
    }
}