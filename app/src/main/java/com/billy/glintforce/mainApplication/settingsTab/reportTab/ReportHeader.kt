package com.billy.glintforce.mainApplication.settingsTab.reportTab

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.data.expenses.Expenses
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.getRequiredStoragePermissions
import com.billy.glintforce.mainApplication.table.TableHeader

/**
 * Composable for header of Report screen
 */
@Composable
fun ReportHeader(
    // Values
    modifier: Modifier = Modifier,
    shortDate: String,
    list: List<Expense>,

    // Actions
    //getDirectoryClick: (Context) -> File,
    requestForegroundPermission: (Context) -> Unit
) {
    val context = LocalContext.current
    val strList = listOf(
        stringResource(id = R.string.expenditureIn),
        stringResource(id = R.string.filterDate),
        stringResource(id = R.string.filterTime),
        stringResource(id = R.string.filterCategory),
        stringResource(id = R.string.details),
        stringResource(id = R.string.amt),
        stringResource(id = R.string.pdfToast),
    )

    // Launcher for permissions to Downloads folder
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            generatePDF(context = context, list = listOf(), strList = strList)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
        
    }

    TableHeader(
        modifier = modifier,
        shortDate = shortDate,
        ui = {
            requestForegroundPermission(context)

            IconButton(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    ),
                onClick = {
                    val requiredPermissions = getRequiredStoragePermissions().toTypedArray()
                    permissionLauncher.launch(requiredPermissions)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    tint = Color.White,
                    contentDescription = "Report download"
                )
            }
        }
    )
}