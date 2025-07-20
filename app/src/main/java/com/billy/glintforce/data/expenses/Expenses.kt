package com.billy.glintforce.data.expenses

// Delete userId if unnecessary
data class Expenses(
    val userId: String = "",
    val category: String = "",
    val desc: String = "",
    val cost: Double = 0.0,
    val date: String = "",
    val time: String = "",
    val month: String = ""
)
