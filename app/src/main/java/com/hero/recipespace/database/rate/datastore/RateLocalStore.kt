package com.hero.recipespace.database.rate.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.database.rate.dao.RateDao
import com.hero.recipespace.listener.OnCompleteListener

class RateLocalStore(
    private val context: Context,
    private val rateDao: RateDao
) : LocalStore<RateData>(context) {

    companion object {
        private lateinit var instance : RateLocalStore

        fun getInstance(context: Context, rateDao: RateDao) : RateLocalStore {
            return instance ?: synchronized(this) {
                instance ?: RateLocalStore(context, rateDao).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RateData>) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }
        val rateKey: String = params[0].toString()
        val rateData: RateData = rateDao.getRateFromKey(rateKey)!!

        kotlin.run {
            onCompleteListener.onComplete(true, rateData)
        }
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RateData>>,
    ) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }

        val rateDataList: LiveData<List<RateData>> = rateDao.getAllRates()

        kotlin.run {
            onCompleteListener.onComplete(true, rateDataList)
        }
    }

    override suspend fun add(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateDao.insertRate(data)

        kotlin.run {
            onCompleteListener.onComplete(true, data)
        }
    }

    override suspend fun update(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateDao.updateRate(data)

        kotlin.run {
            onCompleteListener.onComplete(true, data)
        }
    }

    override suspend fun remove(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateDao.deleteRate(data)

        kotlin.run {
            onCompleteListener.onComplete(true, data)
        }
    }
}