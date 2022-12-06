package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    operator fun invoke(rateKey: String): Flow<RateEntity> =
        rateRepository.getRate(rateKey)
}