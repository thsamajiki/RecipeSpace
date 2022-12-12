package com.hero.recipespace.data.rate.service

import com.hero.recipespace.data.rate.RateData

interface RateService {
    fun getData(rateKey: String) : RateData

    fun getDataList() : List<RateData>

    suspend fun add(recipeKey: String) : RateData

    suspend fun update(rateKey: String) : RateData

    suspend fun remove(rateKey: String) : RateData
}