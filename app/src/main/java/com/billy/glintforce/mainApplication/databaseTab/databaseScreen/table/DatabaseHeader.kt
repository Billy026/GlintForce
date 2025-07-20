package com.billy.glintforce.mainApplication.databaseTab.databaseScreen.table

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.mainApplication.table.TableHeader

/**
 * Composable for header of Database screen
 */
@Composable
fun DatabaseHeader(
    // Values
    modifier: Modifier = Modifier,
    shortDate: String,

    // Actions
    navigateManage: () -> Unit
) {
    TableHeader(
        modifier = modifier,
        shortDate = shortDate,
        ui = {
            Button(
                modifier = Modifier.width(180.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = navigateManage
            ) {
                Text(
                    text = stringResource(id = R.string.manageCategory),
                    color = Color.White
                )
            }
        }
    )
}