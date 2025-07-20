package com.billy.glintforce.mainApplication.homeTab.homeScreen.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billy.glintforce.R

/**
 * Composable for expenditure graph of display section for Home page
 */
@Composable
fun ExpenditureGraph(
    onConfirm: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.expenditure),
        style = MaterialTheme.typography.bodySmall
    )
    Spacer(modifier = Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .size(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onConfirm
        ) {
            Icon(
                painter = painterResource(id = R.drawable.enlarge),
                contentDescription = "enlarge",
                modifier = Modifier.size(50.dp)
            )
        }
        Text(
            text = stringResource(id = R.string.enlargeMessage),
            fontSize = 10.sp
        )
    }
}