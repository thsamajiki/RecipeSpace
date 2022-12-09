package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.UpdateRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val updateRecipeUseCase: UpdateRecipeUseCase
) : AndroidViewModel(application) {

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    val recipeKey: String = savedStateHandle.get<String>(RecipeDetailViewModel.RECIPE_KEY)!!

    suspend fun requestUpdateRecipe(recipeEntity: RecipeEntity) {
        updateRecipeUseCase.invoke(recipeEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}