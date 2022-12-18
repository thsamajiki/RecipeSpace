package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    suspend fun getRate(rateKey: String) : RateEntity

    fun observeRateList() : Flow<List<RateEntity>>

//    suspend fun addRate(recipeKey: String)
//
//    suspend fun addRate(rate: Float, recipeKey: String)

    suspend fun addRate(rateEntity: RateEntity, recipeEntity: RecipeEntity) : RateEntity

    suspend fun modifyRate(request: UpdateRateRequest, rateData: RateData, recipeData: RecipeData) : RateEntity

    suspend fun deleteRate(rateKey: String)
}