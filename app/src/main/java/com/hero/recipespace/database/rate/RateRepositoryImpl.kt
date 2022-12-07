package com.hero.recipespace.database.rate

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.local.RateLocalDataSource
import com.hero.recipespace.data.rate.remote.RateRemoteDataSource
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.mapper.toEntity
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.rate.mapper.toEntity
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RateRepositoryImpl(
    private val rateLocalDataSource: RateLocalDataSource,
    private val rateRemoteDataSource: RateRemoteDataSource
) : RateRepository {

    override fun getRate(
        rateKey: String
    ) : Flow<RateEntity> {
        return rateLocalDataSource.getData(rateKey)
            .map {
                it.toEntity()
            }
    }

    override fun getRateList(): Flow<List<RateEntity>> {
        return rateLocalDataSource.getDataList()
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addRate(
        rateEntity: RateEntity
    ) {
        rateRemoteDataSource.add(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateLocalDataSource.add(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun modifyRate(rateEntity: RateEntity) {

    }

    override suspend fun deleteRate(rateEntity: RateEntity) {

    }

    private fun getEntities(data: List<RateData>): List<RateEntity> {
        val result = mutableListOf<RateEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}