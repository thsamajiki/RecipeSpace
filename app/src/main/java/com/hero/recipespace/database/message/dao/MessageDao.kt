package com.hero.recipespace.database.message.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.message.MessageData

@Dao
interface MessageDao {
    @Query("SELECT * FROM message_db WHERE chatKey = :chatKey")
    fun getMessages(chatKey: String): LiveData<List<MessageData>>

    @Query("SELECT * FROM message_db WHERE messageId = :messageKey limit 1")
    suspend fun getMessageFromKey(messageKey: String): MessageData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageData: MessageData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messageDataList: List<MessageData>)

    @Update
    suspend fun updateMessage(messageData: MessageData)

    @Delete
    suspend fun deleteMessage(messageData: MessageData)
}