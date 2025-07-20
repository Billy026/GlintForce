@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.databaseTab.categories

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.CreateTextField
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.database.CategoryUiState
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryViewModel
import kotlinx.coroutines.launch

/**
 * Containing composable for the main composable of InsertTypeScreen
 */
@Composable
fun InsertCategoryScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,

    // ViewModels
    viewModel: CategoryViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier,
                title = stringResource(id = R.string.insertCategory),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        // UiStates
        //val uiState by viewModel.uiState.collectAsState()

        val keyboardController = LocalSoftwareKeyboardController.current

        CategoryEntryBody(
            //uiState = uiState,
            onValueChange = { /**string -> viewModel.updateDesc(string)**/ },
            buttonString = R.string.addButton,
            contentPadding = innerPadding,
            onClick = { /**
                if (uiState.desc != "") {
                    coroutineScope.launch {
                        viewModel.addCategory()
                        viewModel.resetTextFields()
                    }
                    keyboardController?.hide()
                    navigateBack()
                } **/
            }
        )
    }
}

/**
 * Composable to display all text fields
 */
@Composable
fun CategoryEntryBody(
    // Values
    modifier: Modifier = Modifier,
    @StringRes buttonString: Int,
    contentPadding: PaddingValues,

    // Actions
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,

    // UiStates
    //uiState: CategoryUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        CreateTextField(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            value = "" /**uiState.desc **/,
            onValueChange = { onValueChange(it) },
            label = R.string.detailsRequired,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
        Text(
            text = stringResource(id = R.string.requiredText),
            modifier = Modifier.padding(16.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )

        CategoryButton(
            buttonString = buttonString,
            onClick = onClick,
            //uiState = uiState
        )
    }
}