package com.hero.recipespace.data.rate.service

import com.hero.recipespace.data.rate.RateData
import javax.inject.Inject

class RateServiceImpl @Inject constructor() : RateService {
    override fun getData(rateKey: String): RateData {
        TODO("Not yet implemented")
    }

    override fun getDataList(): List<RateData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(rateData: RateData) {
        TODO("Not yet implemented")
    }

    override suspend fun update(rateData: RateData) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(rateData: RateData) {
        TODO("Not yet implemented")
    }
}