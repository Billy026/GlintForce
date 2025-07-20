package com.billy.glintforce.data.toRemove.goalDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database to store all Goal entities
 */
@Database(entities = [Goal::class], version = 2, exportSchema = false)
abstract class GoalDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var Instance: GoalDatabase? = null

        fun getDatabase(context: Context): GoalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GoalDatabase::class.java, "goals_database")
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}