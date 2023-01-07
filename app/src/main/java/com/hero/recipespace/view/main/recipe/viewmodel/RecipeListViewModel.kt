package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import com.hero.recipespace.domain.recipe.usecase.ObserveRecipeListUseCase
import com.hero.recipespace.domain.recipe.usecase.RefreshRecipeListUseCase
import com.hero.recipespace.util.WLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    application: Application,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val observeRecipeListUseCase: ObserveRecipeListUseCase,
    private val refreshRecipeListUseCase: RefreshRecipeListUseCase
) : AndroidViewModel(application) {

    val recipeList: LiveData<List<RecipeEntity>> = observeRecipeListUseCase().asLiveData()

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    fun refreshRecipeList() {
        viewModelScope.launch {
            refreshRecipeListUseCase()
                .onSuccess {
                    WLog.d("")
                }
                .onFailure {
                    it.printStackTrace()
                    WLog.e("refresh error")
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}