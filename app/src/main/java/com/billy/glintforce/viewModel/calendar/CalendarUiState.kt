package com.billy.glintforce.viewModel.calendar

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Class containing all data needed for date and time
 */
data class CalendarUiState(
    // Current Calendar
    val calendar: Calendar = Calendar.getInstance(),

    val fullDateFormat: SimpleDateFormat
    = SimpleDateFormat("dd MMMM yyyy", Locale.UK),
    val fullCurrentDate: String = fullDateFormat.format(calendar.time),

    val shortDateFormat: SimpleDateFormat
    = SimpleDateFormat("MMMM yyyy", Locale.UK),
    val shortCurrentDate: String = shortDateFormat.format(calendar.time),

    val fullHourTime: SimpleDateFormat
    = SimpleDateFormat("HH:mm", Locale.UK),
    val time: String = fullHourTime.format(calendar.time),

    val dateAndTimeFormat: SimpleDateFormat
    = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.UK),
    val currentDateAndTime: String = dateAndTimeFormat.format(calendar.time),

    val dateFormat: SimpleDateFormat
    = SimpleDateFormat("dd", Locale.UK),
    val currentDate: String = dateFormat.format(calendar.time),

    val monthFormat: SimpleDateFormat
    = SimpleDateFormat("MMMM", Locale.UK),
    val currentMonth: String = monthFormat.format(calendar.time),

    val yearFormat: SimpleDateFormat
    = SimpleDateFormat("yyyy", Locale.UK),
    val currentYear: String = yearFormat.format(calendar.time),

    val dayFormat: SimpleDateFormat
    = SimpleDateFormat("EEE", Locale.UK),
    val currentDay: String = dayFormat.format(calendar.time),
)
