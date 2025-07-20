package com.billy.glintforce.data.toRemove.goalTypeDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data in a Goal Type entity in Goal Type Database
 */
@Entity(tableName = "goalTypes")
data class GoalType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gORr: String,
    val type: String,
    // Value whether GoalType has a maximum or a minimum capacity
    val max: Boolean? = null,
    val editable: Boolean = true
)