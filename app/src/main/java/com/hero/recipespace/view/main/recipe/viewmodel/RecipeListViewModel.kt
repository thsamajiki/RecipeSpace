package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.R
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.ObserveRecipeListUseCase
import com.hero.recipespace.domain.recipe.usecase.RefreshRecipeListUseCase
import com.hero.recipespace.domain.recipe.usecase.SearchRecipeUseCase
import com.hero.recipespace.util.WLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    data class GetSearchedRecipeSuccess(val recipeList: List<RecipeEntity>) : UiState()

    data class GetSearchedRecipeFailed(val message: String) : UiState()

    object Idle : UiState()
}

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    application: Application,
    private val searchRecipeUseCase: SearchRecipeUseCase,
    observeRecipeListUseCase: ObserveRecipeListUseCase,
    private val refreshRecipeListUseCase: RefreshRecipeListUseCase,
) : AndroidViewModel(application) {
    val recipeList: LiveData<List<RecipeEntity>> = observeRecipeListUseCase().asLiveData()

    private val _emptyRecipeListMessage =
        MutableLiveData(getApplication<Application>().getString(R.string.empty_recipe_list_found))
    val emptyRecipeListMessage: LiveData<String>
        get() = _emptyRecipeListMessage

    val searchKeyword: MutableLiveData<String> = MutableLiveData()

    private val _searchRecipeUiState = MutableStateFlow<UiState>(UiState.Idle)
    val searchRecipeUiState: StateFlow<UiState> = _searchRecipeUiState.asStateFlow()

    private val _emptySearchedListMessage =
        MutableLiveData(getApplication<Application>().getString(R.string.no_searched_recipe_found))
    val emptySearchedListMessage: LiveData<String>
        get() = _emptySearchedListMessage


    fun searchRecipe(query: String) {
        viewModelScope.launch {
            searchRecipeUseCase.invoke(query)
                .onSuccess {
                    _searchRecipeUiState.value = UiState.GetSearchedRecipeSuccess(it)
                }
                .onFailure {
                    _searchRecipeUiState.value = UiState.GetSearchedRecipeFailed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

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
}
