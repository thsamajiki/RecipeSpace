package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.usecase.AddRateUseCase
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    application: Application,
    private val getRateUseCase: GetRateUseCase,
    private val addRateUseCase: AddRateUseCase,
    private val updateRateUseCase: UpdateRateUseCase
) : AndroidViewModel(application) {

    val rate: LiveData<RateEntity> = getRateUseCase().asLiveData()

    suspend fun requestAddRateData(rateEntity: RateEntity) {
        addRateUseCase.invoke(rateEntity)
    }

    suspend fun requestUpdateRateData(rateEntity: RateEntity) {
        updateRateUseCase.invoke(rateEntity)
    }

    override fun onCleared() {
        super.onCleared()
    }
}