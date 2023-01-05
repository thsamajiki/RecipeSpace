package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import javax.inject.Inject

class GetRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    suspend operator fun invoke(userKey: String, recipeKey: String): Result<RateEntity> =
        kotlin.runCatching {
            rateRepository.getRate(userKey, recipeKey)
        }
}