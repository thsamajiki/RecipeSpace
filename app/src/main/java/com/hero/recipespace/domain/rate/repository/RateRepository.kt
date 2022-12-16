package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.domain.rate.entity.RateEntity
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    suspend fun getRate(rateKey: String) : RateEntity

    fun observeRateList() : Flow<List<RateEntity>>

    suspend fun addRate(recipeKey: String)

    suspend fun addRate(rate: Float, recipeKey: String)

    suspend fun modifyRate(rateKey: String)

    suspend fun deleteRate(rateKey: String)
}