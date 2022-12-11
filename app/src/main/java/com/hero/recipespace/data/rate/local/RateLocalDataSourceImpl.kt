package com.hero.recipespace.data.rate.local

import androidx.lifecycle.asFlow
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.rate.dao.RateDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RateLocalDataSourceImpl @Inject constructor(
    private val rateDao: RateDao
) : RateLocalDataSource {

    override suspend fun getData(rateKey: String): RateData {
        return rateDao.getRateFromKey(rateKey) ?: error("not found RateData")
    }

    override fun observeDataList(): Flow<List<RateData>> {
        return rateDao.getAllRates().asFlow()
    }

    override suspend fun add(rateData: RateData) {
        rateDao.insertRate(rateData)
    }

    override suspend fun addAll(rateList: List<RateData>) {
        rateDao.insertAll(rateList)
    }

    override suspend fun update(rateData: RateData) {
        rateDao.updateRate(rateData)
    }

    override suspend fun remove(rateData: RateData) {
        rateDao.deleteRate(rateData)
    }

    override fun clear() {

    }
}