package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: RecipeRepository
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    override fun onCleared() {
        super.onCleared()
    }
}