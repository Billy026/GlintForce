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
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import java.util.Locale

/**
 * Card containing details of all Reminders
 */
@Composable
fun OverallRemindersCard(
    // Values
    goalList: List<Goal>,

    // Actions
    navigateEdit: (Int) -> Unit,
    onClick: (Goal) -> Unit
) {
    val reminders = stringResource(id = R.string.reminders)

    if (goalList.any { it.gORr == reminders }) {
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
                    text = stringResource(id = R.string.reminders),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            goalList.filter { it.gORr == reminders }.forEach { reminder ->
                RemindersCard(
                    navigateEdit = { navigateEdit(it.id) },
                    reminder = reminder,
                    onClick = { onClick(reminder) }
                )
            }
        }
    }
}

/**
 * Composable for Card of Reminders
 */
@Composable
private fun RemindersCard(
    // Values
    reminder: Goal,

    // Actions
    navigateEdit: (Goal) -> Unit,
    onClick: () -> Unit
) {
    CreateCard(
        icon = {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(id = R.drawable.reminder),
                contentDescription = "reminder"
            )
        },
        text = {
            Text(
                text = "${reminder.type} - S\$${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        reminder.amount
                    )
                }"
            )
            Text(text = "Desc: ${reminder.desc}")
        },
        navigateOnClick = { navigateEdit(reminder) },
        onClick = onClick
    )
}