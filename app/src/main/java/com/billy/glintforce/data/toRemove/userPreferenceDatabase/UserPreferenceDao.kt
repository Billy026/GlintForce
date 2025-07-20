package com.billy.glintforce.data.toRemove.userPreferenceDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao to store functions to be used to modify user preferences
 */
@Dao
interface UserPreferenceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userPref: UserPreference)

    @Update
    suspend fun update(userPref: UserPreference)

    @Delete
    suspend fun delete(userPref: UserPreference)

    @Query("SELECT * from userPreferences WHERE id = :id")
    fun getItem(id: Int): Flow<UserPreference>

    @Query("SELECT * from userPreferences")
    fun getAllItems(): Flow<List<UserPreference>>

    // Update showWidget property of user preferences
    @Query("UPDATE userPreferences SET showWidget = :showWidget WHERE id = :id")
    suspend fun updateWidget(id: Int, showWidget: Boolean)

    // Update language of user preferences
    @Query("UPDATE userPreferences SET lang = :lang WHERE id = :id")
    suspend fun updateLang(id: Int, lang: String)

    // Update rate at which reports are generated
    @Query("UPDATE userPreferences SET report = :rate WHERE id = :id")
    suspend fun updateReportRate(id: Int, rate: Int)

    // Update reportGenerated property
    @Query("UPDATE userPreferences SET reportGenerated = :generated WHERE id = :id")
    suspend fun updateGeneration(id: Int, generated: Boolean)

    // Update dateGenerated property
    @Query("UPDATE userPreferences SET dateGenerated = :date WHERE id = :id")
    suspend fun updateDate(id: Int, date: String)

    // Update theme property
    @Query("UPDATE userPreferences SET theme = :theme WHERE id = :id")
    suspend fun updateTheme(id: Int, theme: Boolean)
}