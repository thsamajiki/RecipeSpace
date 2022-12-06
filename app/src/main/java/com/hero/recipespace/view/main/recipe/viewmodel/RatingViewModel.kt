package com.hero.recipespace.view.main.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.usecase.AddRateUseCase
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    application: Application,
    private val getRateUseCase: GetRateUseCase,
    private val addRateUseCase: AddRateUseCase
) : AndroidViewModel(application) {

    init {
        viewModelScope.launch {  }
    }

    suspend fun getRateData() {

    }

    suspend fun addRateData(rateEntity: RateEntity) {
        viewModelScope.launch {
            addRateUseCase.invoke(rateEntity)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}