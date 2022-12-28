package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.request.AddRateRequest
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.rate.usecase.AddRateUseCase
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
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
    savedStateHandle: SavedStateHandle,
    private val getRateUseCase: GetRateUseCase,
    private val addRateUseCase: AddRateUseCase,
    private val updateRateUseCase: UpdateRateUseCase,
    private val getRecipeUseCase: GetRecipeUseCase
) : AndroidViewModel(application) {

    companion object {
        const val RECIPE_KEY = "key"
        const val RATE_KEY = "key"
    }

    private val _rateRecipeUiState = MutableStateFlow<RateRecipeUiState>(RateRecipeUiState.Idle)
    val rateRecipeUiState: StateFlow<RateRecipeUiState> = _rateRecipeUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe

    var userKey: MutableLiveData<String> = MutableLiveData()
    val currentRate: MutableLiveData<Float> = MutableLiveData()
    val date: MutableLiveData<Timestamp> = MutableLiveData()

    private val _rate = MutableLiveData<RateEntity>()
    val rate : LiveData<RateEntity>
        get() = _rate

    val recipeKey: String = savedStateHandle.get<String>(RECIPE_KEY)!!

    init {
        getRate()
    }

    private fun getRate() {
        viewModelScope.launch {
            getRateUseCase.invoke(rate.value!!.key)
                .onSuccess {
                    userKey.value = it.userKey
                    currentRate.value = it.rate!!
                    date.value = it.date!!
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    // TODO: 2022-12-18 사용자가 처음으로 Rate 를 add 함
    fun requestAddRateData() {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            val requestRateEntity = rate.value?.copy(rate = currentRate.value)
            addRateUseCase.invoke(AddRateRequest(userKey.value!!, rate.value!!.rate!!), _recipe.value!!)
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _rateRecipeUiState.value = RateRecipeUiState.Success(it)
                    currentRate.value = it.rate!!
                    _rate.value = it
                }
                .onFailure {
                    _loadingState.value = LoadingState.Hidden
                    _rateRecipeUiState.value = RateRecipeUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    fun getRecipeData() {
        viewModelScope.launch {
            getRecipeUseCase.invoke(recipeKey)
        }
    }

    // TODO: 2022-12-18 사용자가 Rate 를 update 함
    fun requestUpdateRateData() {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            val key = rate.value?.key ?: return@launch
            val userKey = rate.value?.userKey ?: return@launch
            val newRate: MutableLiveData<Float> = MutableLiveData()

            updateRateUseCase.invoke(
                UpdateRateRequest(
                    key,
                    userKey,
                    newRate.value!!
                ),
                recipe.value!!
            )
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