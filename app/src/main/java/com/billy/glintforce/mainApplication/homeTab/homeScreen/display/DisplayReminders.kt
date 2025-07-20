package com.billy.glintforce.mainApplication.homeTab.homeScreen.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import java.util.Locale

/**
 * Composable for display of selected Reminders in Display section of Home page
 */
@Composable
fun DisplayReminders(
    list: List<Goal>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        val reminders = stringResource(id = R.string.reminders)
        Text(
            text = stringResource(id = R.string.currReminders),
            style = MaterialTheme.typography.bodySmall
        )

        // Display if any Reminders are set to display
        if (list.filter { it.gORr == reminders }.any { it.display }) {
            RemindersLazyColumn(
                reminders = reminders,
                list = list
            )
        } else {
            // No Reminders set to display
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.noRemindersMsg),
                        textAlign = TextAlign.Justify,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Composable for mutable display of selected Reminders
 */
@Composable
private fun RemindersLazyColumn(
    reminders: String,
    list: List<Goal>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items = list.filter {
            it.gORr == reminders && it.display
        }) { reminder ->
            ReminderBar(reminder = reminder)
        }
    }
}

/**
 * Composable to display chosen Reminders
 */
@Composable
private fun ReminderBar(
    modifier: Modifier = Modifier,
    reminder: Goal
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = "${reminder.type} - S$${
            String.format(
                Locale.getDefault(),
                "%.2f",
                reminder.amount
            )
        }${
            if (reminder.desc != "") {
                " for ${reminder.desc}"
            } else {
                ""
            }
        }"
    )
}