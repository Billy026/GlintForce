package com.billy.glintforce.data.toRemove.userPreferenceDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Interface to be used by all UserPreference Repositories
 */
interface UserPreferenceRepository {
    fun getAllUserPrefsStream(): Flow<List<UserPreference>>

    fun getUserPrefStream(id: Int): Flow<UserPreference?>

    suspend fun insertUserPref(userPref: UserPreference)

    suspend fun deleteUserPref(userPref: UserPreference)

    suspend fun updateWidget(id: Int, showWidget: Boolean)

    suspend fun updateLang(id: Int, lang: String)

    suspend fun updateReportRate(id: Int, rate: Int)

    suspend fun updateGeneration(id: Int, generated: Boolean)

    suspend fun updateDate(id: Int, date: String)

    suspend fun updateTheme(id: Int, theme: Boolean)
}