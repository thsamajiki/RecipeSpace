package com.hero.recipespace.database.rate

import android.content.Context
import com.hero.recipespace.data.RateData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class RateCloudStore : CloudStore<RateData>() {

    companion object {
        private lateinit var instance: RateCloudStore

        fun getInstance(context: Context) : RateCloudStore {
            return instance ?: synchronized(this) {
                instance ?: RateCloudStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<RateData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }
}