package com.hero.recipespace.data.rate.service

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.request.UpdateRateRequest

interface RateService {
    suspend fun getData(rateKey: String, recipeKey: String) : RateData

    fun getDataList() : List<RateData>

//    suspend fun add(recipeKey: String) : RateData
//
//    suspend fun add(rate: Float, recipeKey: String) : RateData

    suspend fun add(rateData: RateData, recipeData: RecipeData) : RateData

    suspend fun update(request: UpdateRateRequest, rateData: RateData, recipeData: RecipeData) : RateData

    suspend fun remove(rateKey: String) : RateData
}