package com.billy.glintforce.mainApplication.homeTab.homeScreen.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.common.ProgressBar
import java.util.Locale

/**
 * Composable for display of selected Goals in Display section of Home page
 */
@Composable
fun DisplayGoals(
    // Values
    list: List<Goal>,
    goalTypeList: List<GoalType>,
    expenseList: List<Expense>,

    // Actions
    navigateDisplay: () -> Unit
) {
    val goals = stringResource(id = R.string.goals)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.currGoals),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.size(width = 80.dp, height = 30.dp),
            onClick = navigateDisplay,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(id = R.string.displayGoals),
                fontSize = 9.sp,
                color = Color.White
            )
        }
    }

    // Display if any Goals are set to display
    if (list.filter { it.gORr == goals }.any { it.display }) {
        GoalsLazyColumn(
            goals = goals,
            list = list,
            goalTypeList = goalTypeList,
            expenseList = expenseList
        )
    } else {
        // No Goals set to display
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.noGoalsMsg),
            textAlign = TextAlign.Justify,
            fontSize = 14.sp
        )
    }
}

/**
 * Composable for mutable display of selected Goals
 */
@Composable
private fun GoalsLazyColumn(
    goals: String,
    list: List<Goal>,
    goalTypeList: List<GoalType>,
    expenseList: List<Expense>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items = list.filter {
            it.gORr == goals && it.display
        }) { goal ->
            ProgressBarRow(
                goal = goal,
                goalTypeList = goalTypeList,
                expenseList = expenseList
            )
        }
    }
}

/**
 * Composable to show progress of selected Goal
 */
@Composable
private fun ProgressBarRow(
    modifier: Modifier = Modifier,
    goal: Goal,
    expenseList: List<Expense>,
    goalTypeList: List<GoalType>
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${goal.type}: S$${String.format(Locale.getDefault(), "%.2f", goal.amount)}${
                if (goal.desc != "") {
                    " (${goal.desc})"
                } else {
                    ""
                }
            }"
        )
        Spacer(modifier = Modifier.height(4.dp))
        ProgressBar(
            goal = goal,
            expenseList = expenseList,
            max = goalTypeList.filter { it.type == goal.type }
        )
    }
}