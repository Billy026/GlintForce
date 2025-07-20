package com.billy.glintforce.data.goalTypeTypeDatabase

import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import kotlinx.coroutines.flow.Flow

/**
* Interface to be used by all GoalType Repositories
*/
interface GoalTypeRepository {
    fun getAllGoalTypesStream(): Flow<List<GoalType>>

    fun getGoalTypeStream(id: Int): Flow<GoalType?>

    suspend fun insertGoalType(goalType: GoalType)

    suspend fun deleteGoalType(goalType: GoalType)

    suspend fun updateGoalType(id: Int, gORr: String, type: String, max: Boolean?)

    suspend fun updateTranslation(id: Int, gORr: String, type: String)
}