package com.billy.glintforce.data.toRemove.expenseDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Interface to be used by all Expense Repositories
 */
interface ExpenseRepository {
    fun getAllExpensesStream(userId: String): Flow<List<Expense>>

    fun getExpenseStream(id: Int, userId: String): Flow<Expense?>

    fun getTodaySpendingStream(todayDate: String, userId: String): Flow<Double?>

    suspend fun insertExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)

    suspend fun updateExpense(id: Int, userId: String, category: String, desc: String, cost: Double)

    suspend fun updateTranslation(id: Int, category: String)
}