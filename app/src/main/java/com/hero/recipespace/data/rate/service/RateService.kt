package com.hero.recipespace.data.rate.service

import com.hero.recipespace.data.rate.RateData

interface RateService {
    suspend fun getData(rateKey: String, recipeKey: String) : RateData

    fun getDataList() : List<RateData>

    suspend fun add(recipeKey: String) : RateData

    suspend fun add(rate: Float, recipeKey: String) : RateData

    suspend fun update(rateKey: String) : RateData

    suspend fun remove(rateKey: String) : RateData
}