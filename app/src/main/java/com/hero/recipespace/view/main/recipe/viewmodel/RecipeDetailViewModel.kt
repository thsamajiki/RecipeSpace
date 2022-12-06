package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData

import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase
) : AndroidViewModel(application) {

    companion object {
        const val RECIPE_KEY = "key"
        const val RECIPE_USER_KEY = "userKey"
    }

    val recipeData: RecipeEntity = savedStateHandle.get<RecipeEntity>(RECIPE_KEY)!!

    val chatData: ChatEntity = savedStateHandle.get<ChatEntity>(RECIPE_USER_KEY)!!

    val recipe: LiveData<RecipeEntity> = getRecipeUseCase(RECIPE_KEY).asLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}