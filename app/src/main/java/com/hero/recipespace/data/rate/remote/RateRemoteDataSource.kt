package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData

interface RateRemoteDataSource {
    suspend fun getData(rateKey: String) : RateData

    suspend fun getDataList() : List<RateData>

    suspend fun add(rateData: RateData)

    suspend fun update(rateData: RateData)

    suspend fun remove(rateData: RateData)
}