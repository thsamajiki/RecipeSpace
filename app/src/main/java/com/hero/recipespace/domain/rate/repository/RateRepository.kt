package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.domain.rate.entity.RateEntity
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    fun getRate(rateKey: String) : Flow<RateEntity>

    fun getRateList() : Flow<List<RateEntity>>

    suspend fun addRate(rateEntity: RateEntity)

    suspend fun modifyRate(rateEntity: RateEntity)

    suspend fun deleteRate(rateEntity: RateEntity)
}