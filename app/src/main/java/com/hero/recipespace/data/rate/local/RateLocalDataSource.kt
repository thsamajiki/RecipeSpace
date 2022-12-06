package com.hero.recipespace.data.rate.local

import com.hero.recipespace.data.rate.RateData
import kotlinx.coroutines.flow.Flow

interface RateLocalDataSource {
    fun getData(rateKey: String) : Flow<RateData>

    fun getDataList() : Flow<List<RateData>>

    fun clear()

    suspend fun add(rateData: RateData)

    suspend fun update(rateData: RateData)

    suspend fun remove(rateData: RateData)
}