package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.UpdateRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    application: Application,
    private val updateRecipeUseCase: UpdateRecipeUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    suspend fun updateRecipe(recipeEntity: RecipeEntity) {

    }

    override fun onCleared() {
        super.onCleared()
    }
}