package com.hero.recipespace.view.post.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val recipeRepository: RecipeRepository
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}