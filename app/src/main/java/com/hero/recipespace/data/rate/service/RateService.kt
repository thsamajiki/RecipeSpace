package com.hero.recipespace.data.rate.service

import com.hero.recipespace.data.rate.RateData

interface RateService {
    fun getData(rateKey: String) : RateData

    fun getDataList() : List<RateData>

    suspend fun add(rateData: RateData)

    suspend fun update(rateData: RateData)

    suspend fun remove(rateData: RateData)
}