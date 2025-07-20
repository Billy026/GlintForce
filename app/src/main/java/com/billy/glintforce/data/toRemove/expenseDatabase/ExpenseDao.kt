package com.billy.glintforce.data.toRemove.expenseDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao to store functions to be used to modify Expense Database
 */
@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * from expenses WHERE id = :id AND userId = :userId")
    fun getExpense(id: Int, userId: String): Flow<Expense>

    @Query("SELECT * from expenses WHERE userId = :userId")
    fun getAllExpenses(userId: String): Flow<List<Expense>>

    // Retrieves the total cost of all Expenses recorded in the day
    @Query("SELECT SUM(cost) FROM expenses WHERE date = :todayDate AND userId = :userId")
    fun getTodaySpending(todayDate: String, userId: String): Flow<Double?>

    // Update an Expense through the Edit Expense Screen
    @Query("UPDATE expenses SET category = :category, `desc` = :desc, cost = :cost WHERE id = :id AND userId = :userId")
    suspend fun updateExpense(id: Int, userId: String, category: String, desc: String, cost: Double)

    // Translate Expenses
    @Query("UPDATE expenses SET category = :category WHERE id = :id")
    suspend fun updateTranslation(id: Int, category: String)
}