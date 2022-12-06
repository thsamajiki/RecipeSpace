package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class AddRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    suspend fun invoke(rateEntity: RateEntity, onCompleteListener: OnCompleteListener<RateEntity>) =
        rateRepository.addRate(rateEntity, onCompleteListener)
}