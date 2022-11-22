package com.hero.recipespace.database.message.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.message.dao.MessageDao

@Database(entities = [MessageData::class], version = 1)
abstract class AppMessageDatabase : RoomDatabase() {
    abstract fun messageDao() : MessageDao

    companion object {
        @Volatile
        private var instance: AppMessageDatabase? = null

        fun getInstance(context: Context): AppMessageDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppMessageDatabase::class.java, "message_db"
            ).build()
    }
}
