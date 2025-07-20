package com.billy.glintforce.mainApplication.homeTab.homeScreen.spending

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.expenses.ExpenseViewModel
import com.billy.glintforce.data.expenses.Expenses
import java.util.Locale

@Composable
fun TodayExpenseDisplay(
    // Values
    paddingValues: PaddingValues,
    list: List<Expense>,

    // Actions
    checkCurrentDate: (String) -> Boolean,

    // ViewModels
    viewModel: ExpenseViewModel = viewModel()
) {
    val expenseList by viewModel.expenseList.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        items(
            items = expenseList
                .filter { expense -> checkCurrentDate(expense.date) }
        ) { expense ->
            ExpenseCard(expense = expense)
        }
    }
}

/**
 * Composable to display individual Expense
 */
@Composable
private fun ExpenseCard(
    modifier: Modifier = Modifier,
    expense: Expenses
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
        ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                val categoryList = listOf(
                    stringResource(id = R.string.foodDrinks),
                    stringResource(id = R.string.transport),
                    stringResource(id = R.string.dailyNecessities),
                    stringResource(id = R.string.leisure),
                    stringResource(id = R.string.misc)
                )

                Icon(
                    modifier = Modifier.padding(8.dp),
                    painter = when (expense.category) {
                        categoryList[0] -> painterResource(id = R.drawable.food_and_drinks)
                        categoryList[1] -> painterResource(id = R.drawable.transport)
                        categoryList[2] -> painterResource(id = R.drawable.daily_necessities)
                        categoryList[3] -> painterResource(id = R.drawable.leisure)
                        categoryList[4] -> painterResource(id = R.drawable.misc)
                        else -> painterResource(id = R.drawable.custom)
                    },
                    contentDescription = expense.category
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${stringResource(id = R.string.filterDate)}: ${expense.date}",
                            fontSize = 12.sp
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${stringResource(id = R.string.filterTime)}: ${expense.time}",
                            fontSize = 12.sp
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${stringResource(id = R.string.filterCategory)}: ${expense.category}",
                        fontSize = 12.sp
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 16.dp)
        ) {
            Text(text = expense.desc, fontSize = 12.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.cost)}: S$ ${
                    String.format(Locale.getDefault(), "%.2f", expense.cost)
                }",
                fontSize = 12.sp
            )
        }
    }
}