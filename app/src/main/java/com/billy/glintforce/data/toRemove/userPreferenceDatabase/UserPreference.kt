package com.billy.glintforce.data.toRemove.userPreferenceDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data to be stored for user preferences
 */
@Entity(tableName = "userPreferences")
data class UserPreference(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val showWidget: Boolean = true,
    val lang: String = "",
    val report: Int = 0,
    val dateGenerated: String = "",
    val reportGenerated: Boolean = false,
    val theme: Boolean = false
)