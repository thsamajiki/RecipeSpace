package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import javax.inject.Inject

class UpdateRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    suspend operator fun invoke(rateEntity: RateEntity) =
        rateRepository.modifyRate(rateEntity)

}