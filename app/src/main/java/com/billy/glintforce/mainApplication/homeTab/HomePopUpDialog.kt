package com.billy.glintforce.mainApplication.homeTab

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.viewModel.screenTemplate.GlintForceUiState
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel

/**
 * Composable to show Pop-Up dialog on Home page reminding users to add widget
 */
@Composable
fun HomePopUpDialog(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    onDismiss: () -> Unit,
    onCheckedChange: ((Boolean) -> Unit)?,

    // UiStates
    uiState: GlintForceUiState,
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.6f)
                .border(1.dp, color = Color.Black, RoundedCornerShape(15.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.widgetNotification1))
                Text(text = "")
                Text(text = stringResource(id = R.string.widgetNotification2))
                Text(text = stringResource(id = R.string.widgetNotification3))
                Text(text = stringResource(id = R.string.widgetNotification4))
                Text(text = stringResource(id = R.string.widgetNotification5))
                Text(text = "")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.checked,
                        onCheckedChange = onCheckedChange
                    )
                    Text(text = stringResource(id = R.string.checkbox))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.width(300.dp),
                        onClick = onDismiss
                    ) {
                        Text(
                            text = stringResource(R.string.confirm),
                            color = if (darkTheme) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}