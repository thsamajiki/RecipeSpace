package com.hero.recipespace.database.rate

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.listener.OnCompleteListener

class RateRepositoryImpl : RateRepository {
    override fun getRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun getRateList(onCompleteListener: OnCompleteListener<List<RateData>>) {
        TODO("Not yet implemented")
    }

    override fun addRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun modifyRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun deleteRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }
}