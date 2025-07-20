@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.screenTemplate

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.billy.glintforce.R
import com.billy.glintforce.mainApplication.tab.TabDatasource.tabList
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.screenTemplate.GlintForceUiState
import com.billy.glintforce.viewModel.screenTemplate.GlintForceViewModel
import com.billy.glintforce.viewModel.settings.UserViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Main TopAppBar above every screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlintForceAppBar(
    // Values
    modifier: Modifier = Modifier,
    navController: NavController,

    // Actions
    navigateLogIn: () -> Unit,

    // UiState
    uiState: GlintForceUiState,

    // ViewModels
    viewModel: GlintForceViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val userName by userViewModel.userName.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { userViewModel.loadUserName(it) }
    }

    TopAppBar(
        modifier = modifier
            .defaultMinSize(minHeight = 100.dp),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Heading is dependent on current page
                val headingText = if (uiState.isSettings) {
                    stringResource(id = R.string.settings_name)
                } else {
                    if (tabList[uiState.selectedTabIndex].heading == R.string.hello) {
                        "${stringResource(id = R.string.hello)}, $userName!"
                    } else {
                        stringResource(tabList[uiState.selectedTabIndex].heading)
                    }
                }

                Text(
                    text = headingText,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                // Button to access Settings Tab
                ProfileIcon(
                    onClick = { /**iconTabs ->**/
                        viewModel.updateTab(index = 4)
                        viewModel.updateSettings(boolean = true)
                        navController.navigate(/**iconTabs.route**/"")
                    },
                    onLogout = {
                        authViewModel.signOut()
                        navigateLogIn()
                    }
                )
            }
        }
    )
}