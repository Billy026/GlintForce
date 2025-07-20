package com.billy.glintforce.data.toRemove.expenseDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data in an Expense entity in Expense Database
 */
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val category: String,
    val desc: String,
    val cost: Double,
    val date: String,
    val time: String,
    val month: String
)
