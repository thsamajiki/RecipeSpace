package com.hero.recipespace.database.chat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.chat.dao.ChatDao
import com.hero.recipespace.ext.TypeConverterExt

@Database(entities = [ChatData::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterExt::class)
abstract class AppChatDatabase : RoomDatabase() {
    abstract fun chatDao() : ChatDao

    companion object {
        @Volatile
        private var instance: AppChatDatabase? = null

        fun getInstance(context: Context): AppChatDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppChatDatabase::class.java, "chat_db"
            ).build()
    }
}