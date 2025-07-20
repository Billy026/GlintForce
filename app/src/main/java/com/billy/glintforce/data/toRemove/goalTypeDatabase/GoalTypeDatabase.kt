package com.billy.glintforce.data.toRemove.goalTypeDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database to store all Goal Type entities
 */
@Database(entities = [GoalType::class], version = 1, exportSchema = false)
abstract class GoalTypeDatabase : RoomDatabase() {
    abstract fun goalTypeDao(): GoalTypeDao

    companion object {
        @Volatile
        private var Instance: GoalTypeDatabase? = null

        fun getDatabase(context: Context): GoalTypeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GoalTypeDatabase::class.java, "goal_types_database")
                    .createFromAsset("database/en/goalTypes.db")
                    .build().also { Instance = it }
            }
        }
    }
}