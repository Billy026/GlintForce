package com.billy.glintforce.mainApplication.screenTemplate

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.billy.glintforce.mainApplication.iconTabs.IconDropDown
import com.billy.glintforce.mainApplication.iconTabs.IconTabs
import com.billy.glintforce.mainApplication.iconTabs.IconTabsDatasource

/**
 * Composable for icon button to navigate to Settings
 */
@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    onClick: (IconTabs) -> Unit,
    onLogout: () -> Unit
) {
    IconDropDown(
        dropDownItems = IconTabsDatasource.iconList,
        onItemClick = { iconTabs: IconTabs ->
            if (iconTabs.route == "logout") {
                onLogout()
            } else {
                onClick(iconTabs)
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
    )
}