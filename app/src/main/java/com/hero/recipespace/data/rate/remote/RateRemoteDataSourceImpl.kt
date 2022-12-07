package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.service.RateService

class RateRemoteDataSourceImpl(
    private val rateService: RateService
) : RateRemoteDataSource {
    override suspend fun getData(rateKey: String): RateData {
        return rateService.getData(rateKey)
    }

    override suspend fun getDataList(): List<RateData> {
        return rateService.getDataList()
    }


    override suspend fun add(rateData: RateData) {
        rateService.add(rateData)
    }

    override suspend fun update(rateData: RateData) {
        rateService.update(rateData)
    }

    override suspend fun remove(rateData: RateData) {
        rateService.remove(rateData)
    }
}