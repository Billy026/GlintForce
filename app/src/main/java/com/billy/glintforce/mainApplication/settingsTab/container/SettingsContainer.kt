package com.billy.glintforce.mainApplication.settingsTab.container

import android.content.Context
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.OfflineUserPreferenceRepository
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.UserPreferenceDatabase
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.UserPreferenceRepository

/**
 * Interface to inject all dependencies into
 */
interface SettingsContainer {
    val userPrefRepository: UserPreferenceRepository
}

/**
 * Data Container to store databases needed for the Settings Tab
 */
class SettingsDataContainer(private val context: Context) : SettingsContainer {
    override val userPrefRepository: UserPreferenceRepository by lazy {
        OfflineUserPreferenceRepository(UserPreferenceDatabase.getDatabase(context).userPrefDao())
    }
}