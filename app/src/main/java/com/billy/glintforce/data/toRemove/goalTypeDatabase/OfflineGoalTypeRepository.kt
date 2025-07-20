package com.billy.glintforce.data.toRemove.goalTypeDatabase

import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Offline instance of Goal Type Repository
 */
class OfflineGoalTypeRepository(private val goalTypeDao: GoalTypeDao) : GoalTypeRepository {
    override fun getAllGoalTypesStream(): Flow<List<GoalType>> = goalTypeDao.getAllItems()

    override fun getGoalTypeStream(id: Int): Flow<GoalType?> = goalTypeDao.getItem(id)

    override suspend fun insertGoalType(goalType: GoalType) = goalTypeDao.insert(goalType)

    override suspend fun deleteGoalType(goalType: GoalType) = goalTypeDao.delete(goalType)

    override suspend fun updateGoalType(id: Int, gORr: String, type: String, max: Boolean?) =
        goalTypeDao.updateGoalType(id = id, gORr = gORr, type = type, max = max)

    override suspend fun updateTranslation(id: Int, gORr: String, type: String) =
        goalTypeDao.updateTranslation(id = id, gORr = gORr, type = type)
}