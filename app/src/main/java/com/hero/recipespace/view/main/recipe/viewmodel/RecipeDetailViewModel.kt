package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    init {
        viewModelScope.launch {
            getRecipeUseCase(recipeKey)
                .onSuccess {
                    _recipe.value = it
                }
                .onFailure {
                    it.printStackTrace()
                    Log.e("RecipeDetailViewModel", "$it ", )
                }
        }
    }

    val recipeKey: String = savedStateHandle.get<String>(RECIPE_KEY)!!

//    val chatKey: String = savedStateHandle.get<String>(RECIPE_USER_KEY)!!

    override fun onCleared() {
        super.onCleared()
    }
}