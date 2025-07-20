package com.billy.glintforce.data.toRemove.goalTypeDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao to store functions to be used to modify Goal Type Database
 */
@Dao
interface GoalTypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goalType: GoalType)

    @Update
    suspend fun update(goalType: GoalType)

    @Delete
    suspend fun delete(goalType: GoalType)

    @Query("SELECT * from goalTypes WHERE id = :id")
    fun getItem(id: Int): Flow<GoalType>

    @Query("SELECT * from goalTypes")
    fun getAllItems(): Flow<List<GoalType>>

    // Update a GoalType through the Edit GoalType Screen
    @Query("UPDATE goalTypes SET gORr = :gORr, type = :type, max = :max WHERE id = :id")
    suspend fun updateGoalType(id: Int, gORr: String, type: String, max: Boolean?)

    // Update translation
    @Query("UPDATE goalTypes SET gORr = :gORr, type = :type WHERE id = :id")
    suspend fun updateTranslation(id: Int, gORr: String, type: String)
}