package com.hero.recipespace.data.rate.local

import com.hero.recipespace.data.rate.RateData
import kotlinx.coroutines.flow.Flow

interface RateLocalDataSource {
    suspend fun getData(rateKey: String): RateData

    fun observeDataList(): Flow<List<RateData>>

    fun clear()

    suspend fun add(rateData: RateData)

    suspend fun addAll(rateList: List<RateData>)

    suspend fun update(rateData: RateData)

    suspend fun remove(rateData: RateData)
}
