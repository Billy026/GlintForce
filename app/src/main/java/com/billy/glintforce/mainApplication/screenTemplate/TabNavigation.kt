package com.billy.glintforce.mainApplication.screenTemplate

import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.billy.glintforce.mainApplication.tab.TabDatasource
import com.billy.glintforce.mainApplication.tab.TabItem

/**
 * Composable for bottom tab selection bar.
 */
@Composable
fun TabNavigation(
    // Values
    selectedTabIndex: Int,

    // Actions
    onClick: (Int, TabItem) -> Unit
) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        TabDatasource.tabList.forEachIndexed { index, item ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onClick(index, item) },
                text = {
                    Text(text = stringResource(id = item.title))
                },
                icon = {
                    Icon(
                        painter = if (index == selectedTabIndex) {
                            painterResource(id = item.selectedIcon)
                        } else {
                            painterResource(id = item.unselectedIcon)
                        },
                        contentDescription = stringResource(id = item.title)
                    )
                }
            )
        }
    }
}