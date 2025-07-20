@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.settingsTab.reportTab

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.common.getMonth
import com.billy.glintforce.mainApplication.table.TableScreen
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.settings.report.ReportUiState
import com.billy.glintforce.viewModel.settings.report.ReportViewModel

// Request code for permission to read and write to external files
private const val REQUEST_FOREGROUND_ONLY_PERMISSION_REQUEST_CODE = 34

// Function to check whether permission for reading and writing to external files have been granted
private fun foregroundPermissionApproved(context: Context): Boolean {
    val writePermissionFlag =
        PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    val readPermissionFlag =
        PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        )

    return writePermissionFlag && readPermissionFlag
}

// Function to get permission to read and write to external files
private fun requestForegroundPermission(context: Context) {
    val provideRationale = foregroundPermissionApproved(context = context)
    if (provideRationale) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_FOREGROUND_ONLY_PERMISSION_REQUEST_CODE
        )
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_FOREGROUND_ONLY_PERMISSION_REQUEST_CODE
        )
    }
}

/**
 * Screen for individual reports for each month
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    //getDirectoryClick: (Context) -> File,
    onNavigateUp: () -> Unit,

    // ViewModels
    reportViewModel: ReportViewModel = viewModel(),
    //expenseRepoViewModel: ExpenseRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel()
) {
    // UiStates
    val uiState by reportViewModel.uiState.collectAsState()
    //val expenseUiState by expenseRepoViewModel.expenseRepoUiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    if (uiState.expenseList == listOf<Expense>()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = stringResource(id = R.string.report),
                    canNavigateBack = canNavigateBack,
                    navigateUp = onNavigateUp,
                )
            }
        ) { innerPadding ->
            val prevYears = /**expenseUiState.expenseList**/listOf<Expense>().filter {
                !it.date.contains(other = calendarUiState.currentYear)
            }
            val currentYear = /**expenseUiState.expenseList**/listOf<Expense>().filter {
                it.date.contains(other = calendarUiState.currentYear)
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentYear.isNotEmpty()) {
                    val context = LocalContext.current
                    val toast = stringResource(id = R.string.reportToast)

                    if (prevYears.isNotEmpty()) {
                        PreviousYearCards(
                            firstDate = /**expenseUiState.expenseList**/listOf<Expense>().first().date,
                            lastDate = /**expenseUiState.expenseList**/listOf<Expense>().last().date,
                            dropDown = { reportViewModel.toggleExpansion(years = "previous") },
                            onClick = { month, year ->
                                val list = prevYears.filter {
                                    it.date.contains(other = month) && it.date.contains(other = year)
                                }
                                if (list.isNotEmpty()) {
                                    reportViewModel.updateExpenseList(
                                        list = list
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        toast,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            uiState = uiState
                        )
                    }
                    CurrentYearCards(
                        fullDate = calendarUiState.fullCurrentDate,
                        dropDown = { reportViewModel.toggleExpansion(years = "current") },
                        onClick = { month ->
                            val list = currentYear.filter { it.date.contains(other = month) }
                            if (list.isNotEmpty()) {
                                reportViewModel.updateExpenseList(
                                    list = list
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    toast,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        uiState = uiState
                    )
                } else {
                    Text(text = stringResource(id = R.string.emptyReport))
                }
            }
        }
    } else {
        val first = uiState.expenseList.first().date.filterIndexed { index, _ -> index > 2 }
        val context = LocalContext.current

        TableScreen(
            modifier = modifier,
            header = {
                ReportHeader(
                    shortDate = first,
                    list = /**expenseUiState.expenseList.**/listOf<Expense>().filter { it.month == first },
                    //getDirectoryClick = getDirectoryClick,
                    requestForegroundPermission = { requestForegroundPermission(context = context) }
                )
            },
            predicate = { it.month == first }
        )
    }
}

/**
 * Composable to display all months with Expenses in previous years
 */
@Composable
private fun PreviousYearCards(
    // Values
    modifier: Modifier = Modifier,
    firstDate: String,
    lastDate: String,

    // Actions
    dropDown: () -> Unit,
    onClick: (String, String) -> Unit,

    // UiStates
    uiState: ReportUiState
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 4.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.small
            ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.prevYears),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = dropDown
                ) {
                    Icon(
                        imageVector = if (uiState.previousExpanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "PreviousYearDropDown"
                    )
                }
            }
            if (uiState.previousExpanded) {
                val firstLen = firstDate.length
                val firstMonth = getMonth(fullDate = firstDate)
                val firstYear = firstDate.filterIndexed { index, _ ->
                    index > firstLen - 4
                }

                val currYear = lastDate.filterIndexed { index, _ ->
                    index > firstLen - 4
                }

                var yearIterator = firstYear.toInt()

                LazyColumn(
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    repeat(times = currYear.toInt() - firstYear.toInt()) {
                        item {
                            if (yearIterator == firstYear.toInt()) {
                                YearLink(
                                    year = yearIterator,
                                    monthList = uiState.monthList.dropWhile { it != firstMonth },
                                    onClick = { month -> onClick(month, yearIterator.toString()) }
                                )
                            } else {
                                YearLink(
                                    year = yearIterator,
                                    monthList = uiState.monthList,
                                    onClick = { month -> onClick(month, yearIterator.toString()) }
                                )
                            }
                        }
                        yearIterator++
                    }
                }
            }
        }
    }
}

/**
 * Composable to display all months with Expenses in the current year
 */
@Composable
private fun CurrentYearCards(
    // Values
    fullDate: String,

    // Actions
    dropDown: () -> Unit,
    onClick: (String) -> Unit,

    // UiStates
    uiState: ReportUiState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 4.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.small
            ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.currYear),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = dropDown
                ) {
                    Icon(
                        imageVector = if (uiState.currentExpanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "CurrentYearDropDown"
                    )
                }
            }
            if (uiState.currentExpanded) {
                val currentMonth = getMonth(fullDate = fullDate)
                val monthList = uiState.monthList.takeWhile { it != currentMonth }

                LazyColumn(
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(items = monthList) { month ->
                        MonthLink(
                            month = month,
                            onClick = { onClick(month) }
                        )
                    }
                    item {
                        MonthLink(
                            month = currentMonth,
                            onClick = { onClick(currentMonth) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Clickable month text that navigates to its respective report
 */
@Composable
private fun MonthLink(
    // Values
    modifier: Modifier = Modifier,
    month: String,

    // Actions
    onClick: () -> Unit
) {
    Text(
        text = month,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
    )
}

/**
 * LazyColumn for years before the current one
 */
@Composable
private fun YearLink(
    // Values
    modifier: Modifier = Modifier,
    year: Int,
    monthList: List<String>,

    // Actions
    onClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = year.toString())
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { expanded = !expanded }) {
                if (expanded) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "DropDownUpwards"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "DropDownDownwards"
                    )
                }
            }
        }

        if (expanded) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items(items = monthList) { month ->
                    MonthLink(
                        month = month,
                        onClick = { onClick(month) }
                    )
                }
            }
        }
    }
}