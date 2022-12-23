package com.hero.recipespace.database.user.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.user.UserData

@Dao
interface UserDao {
    @Query("SELECT * FROM user_db ORDER BY `key` ASC")
    fun getAllUsers() : LiveData<List<UserData>>

    @Query("SELECT * FROM user_db WHERE `key` = :key limit 1")
    suspend fun getUserFromKey(key: String?): UserData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userData: UserData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userDataList: List<UserData>)

    @Update
    suspend fun updateUser(userData: UserData)

    @Delete
    suspend fun deleteUser(userData: UserData)

    @Query("DELETE FROM user_db WHERE `key` = :userId")
    suspend fun deleteUser(userId: String)
}