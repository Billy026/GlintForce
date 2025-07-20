package com.billy.glintforce.viewModel.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Class containing the CalendarUiState and required functions to update and utilise it
 */
class CalendarViewModel : ViewModel() {
    // Most recent CalendarUiState
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    // Checks if given date is equal to the current date
    fun checkCurrentDate(date: String): Boolean {
        return date == uiState.value.fullCurrentDate
    }

    // Checks if expenseDate is greater than goalDate
    fun compareDate(expenseDate: String, goalDate: String): Boolean {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.UK)

        Log.d("debugging", goalDate)
        Log.d("debugging", expenseDate)
        return sdf.parse(expenseDate)!! >= sdf.parse(goalDate)
    }

    // Checks if expenseTime is greater than goalTime
    fun compareTime(expenseTime: String, goalTime: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm", Locale.UK)

        return sdf.parse(expenseTime)!! >= sdf.parse(goalTime)
    }
}