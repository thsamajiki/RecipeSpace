package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.service.RateService
import javax.inject.Inject

class RateRemoteDataSourceImpl @Inject constructor(
    private val rateService: RateService
) : RateRemoteDataSource {
    override suspend fun getData(rateKey: String): RateData {
        return rateService.getData(rateKey)
    }

    override suspend fun getDataList(): List<RateData> {
        return rateService.getDataList()
    }

    override suspend fun add(recipeKey: String) : RateData {
        return rateService.add(recipeKey)
    }

    override suspend fun add(rate: Float, recipeKey: String): RateData {
        return rateService.add(rate, recipeKey)
    }

    override suspend fun update(rateKey: String) : RateData  {
        return rateService.update(rateKey)
    }

    override suspend fun remove(rateKey: String) : RateData {
        return rateService.remove(rateKey)
    }
}