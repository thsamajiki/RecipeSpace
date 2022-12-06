package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import kotlinx.coroutines.flow.Flow

interface RateRemoteDataSource {
    fun getData(rateKey: String) : Flow<RateData>

    fun getDataList() : Flow<List<RateData>>

    suspend fun add(rateData: RateData)

    suspend fun update(rateData: RateData)

    suspend fun remove(rateData: RateData)
}