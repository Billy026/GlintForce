package com.billy.glintforce.data.toRemove.userPreferenceDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Offline instance of Goal Type Repository
 */
class OfflineUserPreferenceRepository(private val userPrefDao: UserPreferenceDao) :
    UserPreferenceRepository {
    override fun getAllUserPrefsStream(): Flow<List<UserPreference>> = userPrefDao.getAllItems()

    override fun getUserPrefStream(id: Int): Flow<UserPreference?> = userPrefDao.getItem(id)

    override suspend fun insertUserPref(userPref: UserPreference) = userPrefDao.insert(userPref)

    override suspend fun deleteUserPref(userPref: UserPreference) = userPrefDao.delete(userPref)

    override suspend fun updateWidget(id: Int, showWidget: Boolean)
      = userPrefDao.updateWidget(id = id, showWidget = showWidget)

    override suspend fun updateLang(id: Int, lang: String)
      = userPrefDao.updateLang(id = id, lang = lang)

    override suspend fun updateReportRate(id: Int, rate: Int)
      = userPrefDao.updateReportRate(id = id, rate = rate)

    override suspend fun updateGeneration(id: Int, generated: Boolean)
      = userPrefDao.updateGeneration(id = id, generated = generated)

    override suspend fun updateDate(id: Int, date: String)
      = userPrefDao.updateDate(id = id, date = date)

    override suspend fun updateTheme(id: Int, theme: Boolean)
      = userPrefDao.updateTheme(id = id, theme = theme)
}