package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeListUseCase
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    application: Application,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getRecipeListUseCase: GetRecipeListUseCase
) : AndroidViewModel(application) {

    val recipeKey = "recipeKey"
    val recipe: LiveData<RecipeEntity> = getRecipeUseCase(recipeKey).asLiveData()
    val recipeList: LiveData<List<RecipeEntity>> = getRecipeListUseCase().asLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}