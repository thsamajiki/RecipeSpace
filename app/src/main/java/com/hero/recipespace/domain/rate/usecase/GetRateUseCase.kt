package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class GetRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    suspend fun invoke(rateKey: String, onCompleteListener: OnCompleteListener<RateEntity>) =
        rateRepository.getRate(rateKey, onCompleteListener)
}