package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.listener.OnCompleteListener

class AddRateUseCase(
    private val rateRepository: RateRepository
) {
    suspend fun invoke(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateRepository.addRate(rateData, onCompleteListener)
    }
}