package com.billy.glintforce.data.toRemove.goalDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao to store functions to be used to modify Goal Database
 */
@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goal: Goal)

    @Update
    suspend fun update(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("SELECT * from goals WHERE id = :id AND userId = :userId")
    fun getItem(id: Int, userId: String): Flow<Goal>

    @Query("SELECT * from goals WHERE userId = :userId")
    fun getAllItems(userId: String): Flow<List<Goal>>

    // Update a Goal through the Edit Goal Screen
    @Query("UPDATE goals SET gORr = :gORr, type = :type, `desc` = :desc, amount = :amount WHERE id = :id AND userId = :userId")
    suspend fun updateGoal(id: Int, userId: String, gORr: String, type: String, desc: String, amount: Double)

    // Update value of display
    @Query("UPDATE goals SET display = :display WHERE id = :id AND userId = :userId")
    suspend fun updateDisplay(id: Int, userId: String, display: Boolean)

    // Update translation
    @Query("UPDATE goals SET gORr = :gORr, type = :type WHERE id = :id")
    suspend fun updateTranslation(id: Int, gORr: String, type: String)
}