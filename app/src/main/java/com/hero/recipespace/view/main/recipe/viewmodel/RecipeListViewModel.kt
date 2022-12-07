package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.ObserveRecipeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    application: Application,
    private val observeRecipeListUseCase: ObserveRecipeListUseCase
) : AndroidViewModel(application) {

    val recipeList: LiveData<List<RecipeEntity>> = observeRecipeListUseCase().asLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}