package com.billy.glintforce.mainApplication.limitTab.limitScreen.goalDisplayScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.common.ProgressBar
import java.util.Locale

/**
 * Card containing details about all Goals
 */
@Composable
fun OverallGoalsCard(
    // Values
    goalList: List<Goal>,
    goalTypeList: List<GoalType>,
    expenseList: List<Expense>,

    // Actions
    navigateEdit: (Int) -> Unit,
    onClick: (Goal) -> Unit
) {
    val goals = stringResource(id = R.string.goals)

    if (goalList.any { it.gORr == goals }) {
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
            ),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.goals),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            goalList.filter { it.gORr == goals }.forEach { goal ->
                GoalsCard(
                    navigateEdit = { navigateEdit(it.id) },
                    goals = goal,
                    expenseList = expenseList,
                    goalTypeList = goalTypeList,
                    onClick = { onClick(goal) }
                )
            }
        }
    }
}

/**
 * Composable for Card of Goals
 */
@Composable
private fun GoalsCard(
    // Values
    goals: Goal,
    expenseList: List<Expense>,
    goalTypeList: List<GoalType>,

    // Actions
    navigateEdit: (Goal) -> Unit,
    onClick: () -> Unit
) {
    CreateCard(
        icon = {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(id = R.drawable.target),
                contentDescription = "target"
            )
        },
        text = {
            Text(
                text = "${goals.type} - S\$${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        goals.amount
                    )
                }"
            )
            Text(text = "${stringResource(id = R.string.details)}: ${goals.desc}")
        },
        navigateOnClick = { navigateEdit(goals) },
        onClick = onClick
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ProgressBar(
            goal = goals,
            expenseList = expenseList,
            max = goalTypeList.filter { it.type == goals.type }
        )
    }
}