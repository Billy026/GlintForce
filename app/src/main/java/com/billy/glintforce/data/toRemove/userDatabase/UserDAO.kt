package com.billy.glintforce.data.toRemove.userDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao to store functions to be used to modify User Database
 */
@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUser(userId: String): Flow<User>

    @Query("UPDATE users SET userName = :userName WHERE userId = :userId")
    suspend fun updateUserName(userId: String, userName: String)
}