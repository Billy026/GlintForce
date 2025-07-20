package com.billy.glintforce.data.toRemove.categoryDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data in a Category entity in Category Database
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val desc: String,
    val editable: Boolean = true
)
