package com.billy.glintforce.data.toRemove.goalDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data in a Goal entity in Goal Database
 */
@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    // Goal or Reminder
    val gORr: String,
    val type: String,
    val desc: String = "",
    val amount: Double,
    val display: Boolean = false,
    val startDate: String,
    val startTime: String
)
