package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.usecase.AddRateUseCase
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RateRecipeUiState {
    data class Success(val rate: RateEntity) : RateRecipeUiState()

    data class Failed(val message: String) : RateRecipeUiState()

    object Idle : RateRecipeUiState()
}

@HiltViewModel
class RatingDialogViewModel @Inject constructor(
    application: Application,
    private val getRateUseCase: GetRateUseCase,
    private val addRateUseCase: AddRateUseCase,
    private val updateRateUseCase: UpdateRateUseCase
) : AndroidViewModel(application) {

    companion object {
        const val RECIPE_KEY = "recipeKey"
        const val RATE_KEY = "rateKey"
    }

    private val _rateRecipeUiState = MutableStateFlow<RateRecipeUiState>(RateRecipeUiState.Idle)
    val rateRecipeUiState: StateFlow<RateRecipeUiState> = _rateRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    private val _rate = MutableLiveData<RateEntity>()
    val rate: LiveData<RateEntity>
        get() = _rate

    val rate2: LiveData<RateEntity> = getRateUseCase(RATE_KEY).asLiveData()

    // TODO: 2022-12-18 사용자가 처음으로 Rate 를 add 함
    fun requestAddRateData(rate: Float) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            addRateUseCase.invoke(_rate.value!!, _recipe.value!!)
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _rateRecipeUiState.value = RateRecipeUiState.Success(it)
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _rateRecipeUiState.value = RateRecipeUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    // TODO: 2022-12-18 사용자가 처음으로 Rate 를 update 함
    fun requestUpdateRateData(rate: Float) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            addRateUseCase.invoke(_rate.value!!, _recipe.value!!)
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _rateRecipeUiState.value = RateRecipeUiState.Success(it)
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _rateRecipeUiState.value = RateRecipeUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}