package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.service.RateService
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import javax.inject.Inject

class RateRemoteDataSourceImpl @Inject constructor(
    private val rateService: RateService
) : RateRemoteDataSource {
    override suspend fun getData(userKey: String, recipeKey: String): RateData {
        return rateService.getData(userKey, recipeKey)
    }

    override suspend fun getDataList(): List<RateData> {
        return rateService.getDataList()
    }

    override suspend fun update(request: UpdateRateRequest, recipeData: RecipeData) : RateData  {
        return rateService.update(request, recipeData)
    }

    override suspend fun remove(rateKey: String) : RateData {
        return rateService.remove(rateKey)
    }
}