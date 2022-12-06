package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.UpdateRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    application: Application,
    private val updateRecipeUseCase: UpdateRecipeUseCase
) : AndroidViewModel(application) {

    suspend fun requestUpdateRecipe(recipeEntity: RecipeEntity) {
        updateRecipeUseCase.invoke(recipeEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}