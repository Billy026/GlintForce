package com.billy.glintforce.mainApplication.table

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable for Generic Header for screens with Expense table
 */
@Composable
fun TableHeader(
    // Values
    modifier: Modifier = Modifier,
    shortDate: String,

    // Actions
    ui: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            text = shortDate,
            fontSize = 20.sp
        )

        ui()
    }
}