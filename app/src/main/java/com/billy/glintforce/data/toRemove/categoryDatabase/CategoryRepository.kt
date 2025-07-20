package com.billy.glintforce.data.toRemove.categoryDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Interface to be used by all Category Repositories
 */
interface CategoryRepository {
    fun getAllCategoriesStream(): Flow<List<Category>>

    fun getCategoryStream(id: Int): Flow<Category?>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(id: Int, desc: String)
}