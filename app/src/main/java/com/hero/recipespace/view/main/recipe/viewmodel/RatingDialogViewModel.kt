package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.usecase.GetRecipeUseCase
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecipeRateUiState {
    data class Success(val rate: RateEntity) : RecipeRateUiState()

    data class Failed(val message: String) : RecipeRateUiState()

    object Idle : RecipeRateUiState()
}

@HiltViewModel
class RatingDialogViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getRateUseCase: GetRateUseCase,
    private val updateRateUseCase: UpdateRateUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val getRecipeUseCase: GetRecipeUseCase
) : AndroidViewModel(application) {

    companion object {
        const val KEY_RECIPE = "key"
    }

    private val _recipeRateUiState = MutableStateFlow<RecipeRateUiState>(RecipeRateUiState.Idle)
    val recipeRateUiState: StateFlow<RecipeRateUiState> = _recipeRateUiState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe: LiveData<RecipeEntity>
        get() = _recipe


    val currentRate: MutableLiveData<Float> = MutableLiveData()
    val date: MutableLiveData<Timestamp> = MutableLiveData()

    private val _rate = MutableLiveData<RateEntity>()
    val rate : LiveData<RateEntity>
        get() = _rate

    val recipeKey: String = savedStateHandle.get<String>(KEY_RECIPE)!!


    // TODO: 사용자의 RateData 를 DB 에서 가져오기 (처음 평가하는 것이면, RateData 가 없을 수도 있음)
    init {
        getRecipeData()

        viewModelScope.launch {
            val userKey = getLoggedUserUseCase().getOrNull()?.key ?: return@launch

            val rateEntity: RateEntity? = getMyRateData(userKey, recipeKey)

            if (rateEntity != null) {
                _rate.value = rateEntity
            }
        }
    }

    private suspend fun getMyRateData(userKey: String, recipeKey: String): RateEntity? {
        return if (userKey.isNotEmpty()) {
            getRateUseCase.invoke(userKey, recipeKey)
                .onFailure {
                    it.printStackTrace()
                }
                .getOrNull()
        } else null
    }

    fun requestUpdateRateData(newRate: Float) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            val userKey = getLoggedUserUseCase().getOrNull()?.key ?: return@launch

            updateRateUseCase.invoke(
                UpdateRateRequest(
                    userKey,
                    newRate
                ),
                recipe.value!!
            )
                .onSuccess {
                    WLog.d("$it")
                    _loadingState.value = LoadingState.Hidden
                    _recipeRateUiState.value = RecipeRateUiState.Success(it)
                    currentRate.value = it.rate
                    _rate.value = it
                }
                .onFailure {
                    WLog.e(it)
                    _loadingState.value = LoadingState.Hidden
                    _recipeRateUiState.value = RecipeRateUiState.Failed(it.message.orEmpty())
                    it.printStackTrace()
                }
        }
    }

    fun getRecipeData() {
        viewModelScope.launch {
            getRecipeUseCase.invoke(recipeKey)
                .onSuccess {
                    _recipe.value = it
                    _rate.value
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}