package com.billy.glintforce.data.toRemove.goalDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Offline instance of Goal Repository
 */
class OfflineGoalRepository(private val goalDao: GoalDao) : GoalRepository {
    override fun getAllGoalsStream(userId: String): Flow<List<Goal>> = goalDao.getAllItems(userId)

    override fun getGoalStream(id: Int, userId: String): Flow<Goal?> = goalDao.getItem(id, userId)

    override suspend fun insertGoal(goal: Goal) = goalDao.insert(goal)

    override suspend fun deleteGoal(goal: Goal) = goalDao.delete(goal)

    override suspend fun updateGoal(
        id: Int,
        userId: String,
        gORr: String,
        type: String,
        desc: String,
        amount: Double
    ) = goalDao.updateGoal(id = id, userId = userId, gORr = gORr, type = type, desc = desc, amount = amount)

    override suspend fun updateDisplay(id: Int, userId: String, display: Boolean) =
        goalDao.updateDisplay(id = id, userId = userId, display = display)

    override suspend fun updateTranslation(id: Int, gORr: String, type: String) =
        goalDao.updateTranslation(id = id, gORr = gORr, type = type)
}