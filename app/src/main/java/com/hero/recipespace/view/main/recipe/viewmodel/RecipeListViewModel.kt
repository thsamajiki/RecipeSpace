package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeListUseCase
import com.hero.recipespace.listener.OnCompleteListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    application: Application,
    private val getRecipeListUseCase: GetRecipeListUseCase,
    onCompleteListener: OnCompleteListener<List<RecipeEntity>>
) : AndroidViewModel(application) {

    private val _recipeList: MutableLiveData<List<RecipeEntity>> = MutableLiveData()
    val recipeList: LiveData<List<RecipeEntity>> = _recipeList

    fun getRecipeList(){
        viewModelScope.launch {
            _recipeList.value = getRecipeListUseCase.invoke(onCompleteListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}