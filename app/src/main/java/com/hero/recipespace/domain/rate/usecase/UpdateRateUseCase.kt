package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import javax.inject.Inject

class UpdateRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    suspend operator fun invoke(request: UpdateRateRequest, rateData: RateData, recipeData: RecipeData) =
        rateRepository.modifyRate(request, rateData, recipeData)
}