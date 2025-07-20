package com.billy.glintforce.data.toRemove.expenseDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Offline instance of Expense Repository
 */
class OfflineExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {

    override fun getAllExpensesStream(userId: String): Flow<List<Expense>> = expenseDao.getAllExpenses(userId)

    override fun getExpenseStream(id: Int, userId: String): Flow<Expense?> = expenseDao.getExpense(id, userId)

    override suspend fun insertExpense(expense: Expense) = expenseDao.insert(expense)

    override suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)

    override suspend fun updateExpense(id: Int, userId: String, category: String, desc: String, cost: Double) =
        expenseDao.updateExpense(id = id, userId = userId, category = category, desc = desc, cost = cost)

    override suspend fun updateTranslation(id: Int, category: String) =
        expenseDao.updateTranslation(id = id, category = category)

    override fun getTodaySpendingStream(todayDate: String, userId: String): Flow<Double?> =
        expenseDao.getTodaySpending(todayDate, userId)
}