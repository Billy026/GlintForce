@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.databaseTab.categories

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.database.CategoryViewModel
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryDetailsViewModel
import kotlinx.coroutines.launch

/**
 * Object containing details on the different Category destinations
 */
object CategoryDetailsDestination {
    private const val ROUTE = "categoryEdit"
    const val CATEGORYIDARG = "categoryId"
    const val ROUTEWITHARGS = "$ROUTE/{$CATEGORYIDARG}"
}

/**
 * Composable for screen to edit existing Category
 */
@Composable
fun EditCategoryScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,

    // ViewModels
    viewModel: CategoryViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    categoryViewModel: CategoryDetailsViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.editCategory),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
    ) { innerPadding ->
        // UiStates
        val uiState by viewModel.uiState.collectAsState()
        val categoryUiState = categoryViewModel.uiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()
        val keyboardController = LocalSoftwareKeyboardController.current

        CategoryEntryBody(
            //uiState = uiState,
            onValueChange = { string -> viewModel.updateDesc(string) },
            buttonString = R.string.editButton,
            contentPadding = innerPadding,
            onClick = {
                if (uiState.desc != "") {
                    coroutineScope.launch {
                        viewModel.updateCategory(
                            category = categoryUiState.value.category,
                            desc = uiState.desc
                        )
                        viewModel.resetTextFields()
                    }
                    keyboardController?.hide()
                    navigateBack()
                }
            }
        )
    }
}