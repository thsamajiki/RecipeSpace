package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.service.RateService
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import javax.inject.Inject

class RateRemoteDataSourceImpl @Inject constructor(
    private val rateService: RateService
) : RateRemoteDataSource {
    override suspend fun getData(rateKey: String, recipeKey: String): RateData {
        return rateService.getData(rateKey, recipeKey)
    }

    override suspend fun getDataList(): List<RateData> {
        return rateService.getDataList()
    }

//    override suspend fun add(recipeKey: String) : RateData {
//        return rateService.add(recipeKey)
//    }
//
//    override suspend fun add(rate: Float, recipeKey: String): RateData {
//        return rateService.add(rate, recipeKey)
//    }

    override suspend fun add(rateData: RateData, recipeData: RecipeData): RateData {
        return rateService.add(rateData, recipeData)
    }

    override suspend fun update(request: UpdateRateRequest, rateData: RateData, recipeData: RecipeData) : RateData  {
        return rateService.update(request, rateData, recipeData)
    }

    override suspend fun remove(rateKey: String) : RateData {
        return rateService.remove(rateKey)
    }
}