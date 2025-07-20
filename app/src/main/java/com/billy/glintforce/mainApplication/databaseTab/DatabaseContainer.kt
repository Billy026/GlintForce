package com.billy.glintforce.mainApplication.databaseTab

import android.content.Context
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryDatabase
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import com.billy.glintforce.data.toRemove.categoryDatabase.OfflineCategoryRepository
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseDatabase
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseRepository
import com.billy.glintforce.data.toRemove.expenseDatabase.OfflineExpenseRepository

/**
 * Interface to inject all dependencies into
 */
interface DatabaseContainer {
    val expenseRepository: ExpenseRepository
    val categoryRepository: CategoryRepository
}

/**
 * Data Container to store categories needed for the Database Tab
 */
class DatabaseDataContainer(private val context: Context) :
    com.billy.glintforce.mainApplication.databaseTab.DatabaseContainer {
    override val expenseRepository: ExpenseRepository by lazy {
        OfflineExpenseRepository(ExpenseDatabase.getDatabase(context).expenseDao())
    }
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(CategoryDatabase.getDatabase(context).categoryDao())
    }
}