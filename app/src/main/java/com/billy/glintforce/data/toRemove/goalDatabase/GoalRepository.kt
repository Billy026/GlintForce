package com.billy.glintforce.data.toRemove.goalDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Interface to be used by all Goal Repositories
 */
interface GoalRepository {
    fun getAllGoalsStream(userId: String): Flow<List<Goal>>

    fun getGoalStream(id: Int, userId: String): Flow<Goal?>

    suspend fun insertGoal(goal: Goal)

    suspend fun deleteGoal(goal: Goal)

    suspend fun updateGoal(id: Int, userId: String, gORr: String, type: String, desc: String, amount: Double)

    suspend fun updateDisplay(id: Int, userId: String, display: Boolean)

    suspend fun updateTranslation(id: Int, gORr: String, type: String)
}