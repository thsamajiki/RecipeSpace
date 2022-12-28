package com.hero.recipespace.domain.rate.usecase

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.rate.request.AddRateRequest
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import javax.inject.Inject

class AddRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    // 혹시 몰라서 만듦
//    suspend operator fun invoke(rate: Float, recipeKey: String) =
//        rateRepository.addRate(rate, recipeKey)

    suspend operator fun invoke(request: AddRateRequest, recipeEntity: RecipeEntity) : Result<RateEntity> =
        kotlin.runCatching {
            rateRepository.addRate(request, recipeEntity)
        }
}