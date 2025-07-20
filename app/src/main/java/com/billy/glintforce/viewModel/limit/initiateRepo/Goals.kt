package com.billy.glintforce.viewModel.limit.initiateRepo

data class Goals(
    val id: Int = 0,
    val userId: String,
    val gORr: String,
    val type: String,
    val desc: String = "",
    val amount: Double,
    val display: Boolean = false,
    val startDate: String,
    val startTime: String
)
