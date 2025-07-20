package com.billy.glintforce.mainApplication.limitTab.goalType

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType

/**
 * Composable to display GoalType list
 */
@Composable
fun GoalTypeDisplay(
    // Values
    modifier: Modifier = Modifier,
    list: List<GoalType>,
    darkTheme: Boolean,

    // Actions
    navigateEdit: (Int) -> Unit,
    onDelete: (GoalType) -> Unit
) {
    val goals = stringResource(id = R.string.goals)
    val reminders = stringResource(id = R.string.reminders)

    LazyColumn(
        modifier = modifier
    ) {
        items(items = list.filter { it.gORr == goals }.sortedBy { it.type }) { item ->
            GoalTypeCard(
                goalType = item,
                navigateEdit = { navigateEdit(item.id) },
                onDelete = { onDelete(item) },
                darkTheme = darkTheme
            )
        }
        items(items = list.filter { it.gORr == reminders }.sortedBy { it.type }) { item ->
            GoalTypeCard(
                goalType = item,
                navigateEdit = { navigateEdit(item.id) },
                onDelete = { onDelete(item) },
                darkTheme = darkTheme
            )
        }
    }
}

/**
 * Composable to create a card to display details about a GoalType
 */
@Composable
private fun GoalTypeCard(
    // Values
    modifier: Modifier = Modifier,
    goalType: GoalType,
    darkTheme: Boolean,

    // Actions
    navigateEdit: (GoalType) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 4.dp,
                color = if (darkTheme) Color.White else Color.Black,
                shape = MaterialTheme.shapes.small
            ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Displays additional details if goalType.gORr is "Goals"
                Text(
                    text = "${goalType.gORr}${
                        if (goalType.max != null) {
                            "            ${stringResource(id = R.string.maxMin)}: ${
                                if (goalType.max) {
                                    stringResource(id = R.string.max)
                                } else {
                                    stringResource(id = R.string.min)
                                }
                            }"
                        } else {
                            ""
                        }
                    }"
                )
                Text(text = "${stringResource(id = R.string.typeDesc)}: ${goalType.type}")
            }
            // Prevents starting Goals/Reminders from being edited and/or deleted
            if (goalType.editable) {
                IconButton(onClick = { navigateEdit(goalType) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}