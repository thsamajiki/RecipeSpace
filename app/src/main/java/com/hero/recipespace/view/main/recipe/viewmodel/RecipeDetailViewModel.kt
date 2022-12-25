package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.chat.usecase.AddChatUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecipeDetailUIState {
    data class Success(val recipeEntity: RecipeEntity) : RecipeDetailUIState()

    data class Failed(val message: String) : RecipeDetailUIState()
}

sealed class IntentChatUIState {
    object Success : IntentChatUIState()

    data class Failed(val message: String) : IntentChatUIState()
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val addChatUseCase: AddChatUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val getRecipeUseCase: GetRecipeUseCase
) : AndroidViewModel(application) {

    companion object {
        const val RECIPE_KEY = "key"
        const val RECIPE_USER_KEY = "userKey"
        const val RECIPE_ENTITY = "recipeEntity"
    }

    private val _recipeDetailUiState = MutableLiveData<RecipeDetailUIState>()
    val recipeDetailUiState: LiveData<RecipeDetailUIState>
        get() = _recipeDetailUiState

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    val recipeKey: String = savedStateHandle.get<String>(RECIPE_KEY)!!

//    val recipeEntity: RecipeEntity = savedStateHandle.get<RecipeEntity>(RECIPE_ENTITY)!!

    init {
        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess {
                    _recipe.value = it
                    Log.d("zxc", "RecipeDetailViewModel recipeKey: $recipeKey")
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.key}: ${recipe.value!!.key}")
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.desc}: ${recipe.value!!.desc}")
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.userKey}: ${recipe.value!!.userKey}")
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.userName}: ${recipe.value!!.userName}")
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.profileImageUrl}: ${recipe.value!!.profileImageUrl}")
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