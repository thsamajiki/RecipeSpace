package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : AndroidViewModel(application) {

    companion object {
        const val RECIPE_KEY = "key"
        const val RECIPE_USER_KEY = "userKey"
    }

    val recipeData: RecipeData = savedStateHandle.get<RecipeData>(RECIPE_KEY)!!

    val chatData: ChatData = savedStateHandle.get<ChatData>(RECIPE_USER_KEY)!!

    override fun onCleared() {
        super.onCleared()
    }
}