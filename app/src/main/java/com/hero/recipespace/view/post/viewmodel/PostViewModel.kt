package com.hero.recipespace.view.post.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.AddRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val addRecipeUseCase: AddRecipeUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    suspend fun postRecipe(recipeEntity: RecipeEntity) {
        viewModelScope.launch {
            addRecipeUseCase.invoke(recipeEntity)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}