package com.billy.glintforce.data.toRemove.userPreferenceDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database to store user preferences
 */
@Database(entities = [UserPreference::class], version = 1, exportSchema = false)
abstract class UserPreferenceDatabase : RoomDatabase() {
    abstract fun userPrefDao(): UserPreferenceDao

    companion object {
        @Volatile
        private var Instance: UserPreferenceDatabase? = null

        fun getDatabase(context: Context): UserPreferenceDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    UserPreferenceDatabase::class.java,
                    "user_pref_database"
                )
                    .createFromAsset("database/en/userPreferences.db")
                    .build().also { Instance = it }
            }
        }
    }
}