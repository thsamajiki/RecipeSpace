package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.request.AddRateRequest
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    suspend fun getRate(rateKey: String) : RateEntity

    fun observeRateList() : Flow<List<RateEntity>>

    suspend fun addRate(request: AddRateRequest, recipeEntity: RecipeEntity) : RateEntity

    suspend fun modifyRate(request: UpdateRateRequest, recipeEntity: RecipeEntity) : RateEntity

    suspend fun deleteRate(rateKey: String)
}