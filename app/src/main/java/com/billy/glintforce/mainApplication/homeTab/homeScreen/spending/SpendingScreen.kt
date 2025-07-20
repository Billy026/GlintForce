package com.billy.glintforce.mainApplication.homeTab.homeScreen.spending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.common.FloatingAddButton
import com.billy.glintforce.common.TopAppBar
import java.util.Locale

/**
 * Composable to show Today's Spending portion of the Home Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingScreen(
    // Values
    modifier: Modifier = Modifier,
    list: List<Expense>,

    // Actions
    checkCurrentDate: (String) -> Boolean,
    navigateAdd: () -> Unit,
) {
    // Value for total expenditure for the current date
    val currList = list.filter { checkCurrentDate(it.date) }
    val currentTotal = if (currList.isNotEmpty()) {
        currList
            .map { it.cost }
            .reduce { acc, d -> acc + d }
    } else {
        0.0
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        topBar = {
            TopAppBar(
                title = "${stringResource(id = R.string.today_spending)} S$${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        currentTotal
                    )
                }",
                canNavigateBack = false,
                topAppBarColor = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                color = Color.Black
            )
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = navigateAdd,
                contentDescription = "Add Expense"
            )
        },
        containerColor = Color.Transparent
    ) {
        TodayExpenseDisplay(
            paddingValues = it,
            list = list,
            checkCurrentDate = checkCurrentDate
        )
    }
}