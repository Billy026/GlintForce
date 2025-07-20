package com.billy.glintforce.data.toRemove.categoryDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Offline instance of Category Repository
 */
class OfflineCategoryRepository(private val categoryDao: CategoryDao) : CategoryRepository {
    override fun getAllCategoriesStream(): Flow<List<Category>> = categoryDao.getAllCategories()

    override fun getCategoryStream(id: Int): Flow<Category?> = categoryDao.getCategory(id)

    override suspend fun insertCategory(category: Category) = categoryDao.insert(category)

    override suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    override suspend fun updateCategory(id: Int, desc: String) =
        categoryDao.updateCategory(id = id, desc = desc)
}