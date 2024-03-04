package com.hero.recipespace.database.chat.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hero.recipespace.data.chat.ChatData

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_db")
    fun getChats(): LiveData<List<ChatData>>

    @Query("SELECT * FROM chat_db WHERE `key` = :key limit 1")
    suspend fun getChatFromKey(key: String?): ChatData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chatData: ChatData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chatDataList: List<ChatData>)

    @Update
    suspend fun updateChat(chatData: ChatData)

    @Delete
    suspend fun deleteChat(chatData: ChatData)
}
