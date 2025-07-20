@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.databaseTab.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.categoryDatabase.Category
import com.billy.glintforce.common.FloatingAddButton
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryRepoUiState
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryRepoViewModel
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryViewModel
import kotlinx.coroutines.launch

/**
 * Composable to display all existing Categories
 */
@Composable
fun ManageCategoriesScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    navigateAdd: () -> Unit,
    navigateEdit: (Int) -> Unit,
    onNavigateUp: () -> Unit,

    // ViewModels
    viewModel: CategoryViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    repoViewModel: CategoryRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.manageCategory),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
        floatingActionButton = { FloatingAddButton(
            onClick = navigateAdd,
            contentDescription = "Add Category"
        ) }
    ) { innerPadding ->
        // UiStates
        val categoryRepoUiState by repoViewModel.categoryRepoUiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        ManageCategoryEntryBody(
            modifier = modifier,
            categoryRepo = categoryRepoUiState,
            navigateEdit = navigateEdit,
            contentPadding = innerPadding,
            onDelete = {
                category -> coroutineScope.launch {
                    //viewModel.deleteCategory(category = category)
                }
            }
        )
    }
}

/**
 * Composable that deals with if Category list is empty
 */
@Composable
private fun ManageCategoryEntryBody(
    // Values
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,

    // Actions
    navigateEdit: (Int) -> Unit,
    onDelete: (Category) -> Unit,

    // ViewModels
    categoryRepo: CategoryRepoUiState,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Displays message if GoalType list is empty
        if (categoryRepo.categoryList.isEmpty()) {
            Text(
                text = "No additional categories added yet. Press the + button to add one!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
            )
        } else {
            // Displays items in Category list
            CategoryDisplay(
                list = categoryRepo.categoryList,
                navigateEdit = navigateEdit,
                onDelete = onDelete
            )
        }
    }
}