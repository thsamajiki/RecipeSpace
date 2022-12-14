package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.usecase.AddRateUseCase
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _rate = MutableLiveData<RateEntity>()
    val rate: LiveData<RateEntity>
        get() = _rate

    val rate2: LiveData<RateEntity> = getRateUseCase().asLiveData()

    suspend fun requestAddRateData(rateEntity: RateEntity) {
        addRateUseCase.invoke(rateEntity.key)
    }

    suspend fun requestUpdateRateData(rateEntity: RateEntity) {
        updateRateUseCase.invoke(rateEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}