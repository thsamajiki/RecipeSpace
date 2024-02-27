package com.hero.recipespace.database.rate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.rate.dao.RateDao
import com.hero.recipespace.ext.TypeConverterExt

@Database(entities = [RateData::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterExt::class)
abstract class AppRateDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao

    companion object {
        @Volatile
        private var instance: AppRateDatabase? = null

        fun getInstance(context: Context): AppRateDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRateDatabase::class.java,
                "rate_db",
            ).build()
    }
}
