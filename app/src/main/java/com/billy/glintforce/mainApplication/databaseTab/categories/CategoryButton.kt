package com.billy.glintforce.mainApplication.databaseTab.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.viewModel.database.CategoryUiState
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel

/**
 * Composable for button in AddCategoryScreen and EditCategoryScreen
 */
@Composable
fun CategoryButton(
    // Values
    buttonString: Int,

    // Actions
    onClick: () -> Unit,

    // UiStates
    //uiState: CategoryUiState,

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
    
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                // Changes color depending if required text fields are filled
                containerColor = if ( true
                    //uiState.desc != ""
                ) {
                    if (darkTheme) Color.White else Color.Black
                } else {
                    if (darkTheme) Color.DarkGray else Color.LightGray
                }
            ),
            modifier = Modifier
                .padding(8.dp)
                .defaultMinSize(minWidth = 300.dp)
        ) {
            Text(
                text = stringResource(id = buttonString),
                color = if (darkTheme) Color.Black else Color.White
            )
        }
    }
}