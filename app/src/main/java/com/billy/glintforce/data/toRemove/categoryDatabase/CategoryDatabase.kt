package com.billy.glintforce.data.toRemove.categoryDatabase

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database to store all Category entities
 */
@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var Instance: CategoryDatabase? = null

        fun getDatabase(context: Context): CategoryDatabase {
            val locale = Locale.current.language
            Log.d("localeLang", locale)

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    CategoryDatabase::class.java,
                    "categories_database"
                )
                    .createFromAsset("database/en/categories.db")
                    .build().also { Instance = it }
            }
        }
    }
}