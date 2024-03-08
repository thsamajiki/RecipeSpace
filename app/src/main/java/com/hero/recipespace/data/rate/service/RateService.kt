package com.hero.recipespace.data.rate.service

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.request.UpdateRateRequest

interface RateService {
    suspend fun getData(
        userKey: String,
        recipeKey: String,
    ): RateData

    fun getDataList(): List<RateData>

    suspend fun update(
        request: UpdateRateRequest,
        recipeData: RecipeData,
    ): RateData

    suspend fun remove(rateKey: String): RateData
}
