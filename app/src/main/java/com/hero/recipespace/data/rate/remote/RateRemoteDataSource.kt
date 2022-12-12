package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData

interface RateRemoteDataSource {
    suspend fun getData(rateKey: String) : RateData

    suspend fun getDataList() : List<RateData>

    suspend fun add(recipeKey: String) : RateData

    suspend fun update(rateKey: String) : RateData

    suspend fun remove(rateKey: String) : RateData
}