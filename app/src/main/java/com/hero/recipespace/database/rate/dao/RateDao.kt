package com.hero.recipespace.database.rate.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.rate.RateData

@Dao
interface RateDao {
    @Query("SELECT * FROM rate_db ORDER BY key ASC")
    fun getAllRates() : LiveData<List<RateData>>

    @Query("SELECT * FROM rate_db WHERE key = :key limit 1")
    suspend fun getRateFromKey(key: String?): RateData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRate(messageData: MessageData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rateDataList: List<RateData>)

    @Update
    fun updateRate(rateData: RateData)

    @Delete
    fun deleteRate(rateData: RateData)
}