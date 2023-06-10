package com.hero.recipespace.view.main.recipe.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
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

sealed class OpenRateUIState {
    object Success : OpenRateUIState()

    data class Failed(val message: String) : OpenRateUIState()
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase
) : ViewModel() {

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

    val profileImageUrl: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    val postDate: MutableLiveData<Timestamp?> = MutableLiveData()
    val desc: MutableLiveData<String> = MutableLiveData()

    val rate: MutableLiveData<Float?> = MutableLiveData()
    val photoUrlList: MutableLiveData<List<String>> = MutableLiveData()

    val recipeKey: String = savedStateHandle.get<String>(RECIPE_KEY)!!

//    val recipeEntity: RecipeEntity = savedStateHandle.get<RecipeEntity>(RECIPE_ENTITY)!!

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
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.userName}: ${recipe.value!!.userName}")
                    Log.d("zxc", "RecipeDetailViewModel {recipe.value!!.profileImageUrl}: ${recipe.value!!.profileImageUrl}")
                }
                .onFailure {
                    it.printStackTrace()
                    Log.e("RecipeDetailViewModel", "$it ")
                }
        }
//
//        viewModelScope.launch {
//            getRecipeUseCase(recipeKey)
//                .onSuccess {
//                    _recipeDetailUiState.value = RecipeDetailUIState.Success(it)
//                }
//                .onFailure {
//                    _recipeDetailUiState.value = RecipeDetailUIState.Failed(it.message.orEmpty())
//                    it.printStackTrace()
//                }
//        }
    }

    fun openRate() {
        viewModelScope.launch {

        }
    }

//    val chatKey: String = savedStateHandle.get<String>(RECIPE_USER_KEY)!!

    override fun onCleared() {
        super.onCleared()
    }
}