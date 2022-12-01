package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: RecipeRepository
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}