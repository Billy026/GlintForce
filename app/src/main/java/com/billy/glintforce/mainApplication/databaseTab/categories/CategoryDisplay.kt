package com.billy.glintforce.mainApplication.databaseTab.categories

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.data.toRemove.categoryDatabase.Category
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel

/**
 * Composable to display Category list
 */
@Composable
fun CategoryDisplay(
    // Values
    modifier: Modifier = Modifier,
    list: List<Category>,

    // Actions
    navigateEdit: (Int) -> Unit,
    onDelete: (Category) -> Unit,

    // ViewModels
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()
    
    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(items = list.filter { !it.editable }.sortedBy { it.desc }) {item->
            CategoryCard(
                category = item,
                navigateEdit = { navigateEdit(item.id) },
                onDelete = { onDelete(item) },
                darkTheme = darkTheme
            )
        }
        items(items = list.filter { it.editable }.sortedBy { it.desc }) {item ->
            CategoryCard(
                category = item,
                navigateEdit = { navigateEdit(item.id) },
                onDelete = { onDelete(item) },
                darkTheme = darkTheme
            )
        }
    }
}

/**
 * Composable to create a card to display details about a Category
 */
@Composable
private fun CategoryCard(
    // Values
    modifier: Modifier = Modifier,
    category: Category,

    // Actions
    navigateEdit: (Category) -> Unit,
    onDelete: () -> Unit,
    darkTheme: Boolean
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
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = category.desc)
            }
            // Prevents starting Goals/Reminders from being edited and/or deleted
            if (category.editable) {
                IconButton(onClick = { navigateEdit(category) }) {
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