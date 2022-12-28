package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import javax.inject.Inject

class UpdateRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    suspend operator fun invoke(request: UpdateRateRequest, recipeEntity: RecipeEntity) : Result<RateEntity> =
        kotlin.runCatching {
            rateRepository.modifyRate(request, recipeEntity)
        }
}