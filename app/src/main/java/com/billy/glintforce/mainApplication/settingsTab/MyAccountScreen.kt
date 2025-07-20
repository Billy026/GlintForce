package com.billy.glintforce.mainApplication.settingsTab

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.settings.UserViewModel

/**
 * Screen to change user's username
 */
@Composable
fun MyAccountScreen(
    viewModel: UserViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    authViewModel: AuthViewModel
) {
    // UiStates
    val userName by viewModel.userName.collectAsState()

    val currentUser = authViewModel.getCurrentUserEmail()
    var newUserName by remember { mutableStateOf(userName) }
    val context = LocalContext.current

    val resetEmail = stringResource(id = R.string.resetEmail)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
    ) {
        Text(text = stringResource(id = R.string.currUsername))
        OutlinedTextField(
            value = userName,
            onValueChange = {},
            readOnly = true,
            label = { R.string.currName },
            modifier = Modifier.padding(16.dp)
        )

        Text(text = stringResource(id = R.string.newUsername))
        OutlinedTextField(
            value = newUserName,
            onValueChange = { newUserName = it },
            label = { R.string.newName },
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = { viewModel.updateUserName(newUserName) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.update),
                color = Color.White
            )
        }

        Button(
            onClick = {
                if (currentUser != null) {
                    authViewModel.sendPasswordResetEmail(currentUser, true)
                }
                Toast.makeText(
                    context,
                    resetEmail,
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(text = stringResource(R.string.resetPassword))
        }
    }
}