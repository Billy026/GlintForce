package com.billy.glintforce.mainApplication.limitTab

import android.content.Context
import com.billy.glintforce.data.toRemove.goalDatabase.GoalDatabase
import com.billy.glintforce.data.toRemove.goalDatabase.GoalRepository
import com.billy.glintforce.data.toRemove.goalDatabase.OfflineGoalRepository
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalTypeDatabase
import com.billy.glintforce.data.toRemove.goalTypeDatabase.OfflineGoalTypeRepository
import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository

/**
 * Interface to inject all dependencies into
 */
interface LimitContainer {
     val goalRepository: GoalRepository
     val goalTypeRepository: GoalTypeRepository
}

/**
 * Data Container to store databases needed for the Limit Tab
 */
class LimitDataContainer(private val context: Context) : LimitContainer {
    override val goalRepository: GoalRepository by lazy {
        OfflineGoalRepository(GoalDatabase.getDatabase(context).goalDao())
    }
    override val goalTypeRepository: GoalTypeRepository by lazy {
        OfflineGoalTypeRepository(GoalTypeDatabase.getDatabase(context).goalTypeDao())
    }
}