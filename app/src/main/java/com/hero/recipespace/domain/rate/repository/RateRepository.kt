package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    suspend fun getRate(rateKey: String): RateEntity

    suspend fun getRate(
        userKey: String,
        recipeKey: String,
    ): RateEntity

    fun observeRateList(): Flow<List<RateEntity>>

    suspend fun submitRate(
        request: UpdateRateRequest,
        recipeEntity: RecipeEntity,
    ): RateEntity

    suspend fun deleteRate(rateKey: String)
}
