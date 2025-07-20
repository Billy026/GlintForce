package com.billy.glintforce.mainApplication.iconTabs

import androidx.annotation.StringRes

/**
 * Class containing all data for the IconTab
 */
data class IconTabs(
    @StringRes val title: Int,
    val isLogOut: Boolean = false,
    val route: String = ""
)
