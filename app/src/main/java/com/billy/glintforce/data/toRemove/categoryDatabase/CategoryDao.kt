package com.billy.glintforce.data.toRemove.categoryDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao to store functions to be used to modify Category Database
 */
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * from categories WHERE id = :id")
    fun getCategory(id: Int): Flow<Category>

    @Query("SELECT * from categories")
    fun getAllCategories(): Flow<List<Category>>

    // Update a Category through the Edit Category Screen
    @Query("UPDATE categories SET `desc` = :desc WHERE id = :id")
    suspend fun updateCategory(id: Int, desc: String)
}