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
import com.hero.recipespace.domain.user.usecase.GetLoggedUserUseCase
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
    private val addRateUseCase: AddRateUseCase,
    private val updateRateUseCase: UpdateRateUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val getRecipeUseCase: GetRecipeUseCase
) : AndroidViewModel(application) {

    companion object {
        const val KEY_RECIPE = "key"
        const val KEY_RATE = "userKey"
    }

    private val _recipeRateUiState = MutableStateFlow<RecipeRateUiState>(RecipeRateUiState.Idle)
    val recipeRateUiState: StateFlow<RecipeRateUiState> = _recipeRateUiState.asStateFlow()

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

    val recipeKey: String = savedStateHandle.get<String>(KEY_RECIPE)!!


    // TODO: 사용자의 RateData 를 DB 에서 가져오기 (처음 평가하는 것이면, RateData 가 없을 수도 있음)
    init {
        val rateKey: String = savedStateHandle.get<String>(KEY_RATE).orEmpty()

        viewModelScope.launch {
            val rateEntity: RateEntity? = getMyRateData(rateKey)

            if (rateEntity == null) {
                // TODO: requestAddRateData 를 하기
            } else {
                // TODO: requestUpdateRateData 를 하기
            }
        }
    }

    private suspend fun getMyRateData(userKey: String): RateEntity? {
        return if (userKey.isNotEmpty()) {
            getRateUseCase.invoke(userKey)
                .onFailure {
                    it.printStackTrace()
                }
                .getOrNull()
        } else null
    }

    // TODO: 사용자가 처음으로 Rate 를 add 함
    fun requestAddRateData(
        rateKey: String,
        rate: Float
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            if (rate != 0f) {
                if (rateKey.isNotEmpty()) {
                    addRateUseCase.invoke(AddRateRequest(rateKey, rate), _recipe.value!!)
                        .onSuccess {
                            _loadingState.value = LoadingState.Hidden
                            _recipeRateUiState.value = RecipeRateUiState.Success(it)
                            currentRate.value = it.rate!!
                            _rate.value = it
                        }
                        .onFailure {
                            _loadingState.value = LoadingState.Hidden
                            _recipeRateUiState.value = RecipeRateUiState.Failed(it.message.orEmpty())
                            it.printStackTrace()
                        }
                }
            }

        }
    }

    // TODO: 사용자가 Rate 를 update 함
    fun requestUpdateRateData(
        userKey: String,
        newRate: Float
    ) {
        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {
//            val key = rate.value?.userKey ?: return@launch
//            val userKey = rate.value?.userKey ?: return@launch
//            val newRate: MutableLiveData<Float> = MutableLiveData()

            updateRateUseCase.invoke(
                UpdateRateRequest(
                    userKey,
                    newRate
                ),
                recipe.value!!
            )
                .onSuccess {
                    _loadingState.value = LoadingState.Hidden
                    _recipeRateUiState.value = RecipeRateUiState.Success(it)
                }
                .onFailure {
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