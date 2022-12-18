package com.hero.recipespace.database.rate

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.local.RateLocalDataSource
import com.hero.recipespace.data.rate.remote.RateRemoteDataSource
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.mapper.toData
import com.hero.recipespace.domain.rate.mapper.toEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.rate.request.UpdateRateRequest
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.mapper.toData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RateRepositoryImpl @Inject constructor(
    private val rateLocalDataSource: RateLocalDataSource,
    private val rateRemoteDataSource: RateRemoteDataSource
) : RateRepository {

    override suspend fun getRate(rateKey: String) : RateEntity {
        return rateLocalDataSource.getData(rateKey).toEntity()
    }

    override fun observeRateList(): Flow<List<RateEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            val rateList = rateRemoteDataSource.getDataList()
            rateLocalDataSource.addAll(rateList)
            cancel()
        }

        return rateLocalDataSource.observeDataList()
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

//    override suspend fun addRate(recipeKey: String) {
//        val result = rateRemoteDataSource.add(recipeKey)
//        rateLocalDataSource.add(result)
//    }
//
//    // 혹시 필요할지 몰라서 만듦
//    override suspend fun addRate(rate: Float, recipeKey: String) {
//        val result = rateRemoteDataSource.add(rate, recipeKey)
//        rateLocalDataSource.add(result)
//    }

    override suspend fun addRate(rateEntity: RateEntity, recipeEntity: RecipeEntity): RateEntity {
        val result = rateRemoteDataSource.add(rateEntity.toData(), recipeEntity.toData())
        rateLocalDataSource.add(result)

        return result.toEntity()
    }

    override suspend fun modifyRate(request: UpdateRateRequest, rateData: RateData, recipeData: RecipeData): RateEntity {
        val result = rateRemoteDataSource.update(request, rateData, recipeData)
        rateLocalDataSource.update(result)

        return result.toEntity()
    }

    override suspend fun deleteRate(rateKey: String) {
        val result = rateRemoteDataSource.remove(rateKey)
        rateLocalDataSource.remove(result)
    }

    private fun getEntities(data: List<RateData>): List<RateEntity> {
        val result = mutableListOf<RateEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}