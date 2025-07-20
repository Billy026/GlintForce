package com.billy.glintforce.data.toRemove.expenseDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database to store all Expense entities
 */
@Database(entities = [Expense::class], version = 2, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var Instance: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ExpenseDatabase::class.java, "expenses_database")
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}