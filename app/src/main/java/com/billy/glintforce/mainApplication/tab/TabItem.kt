package com.billy.glintforce.mainApplication.tab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Class containing all data for a TabItem
 */
data class TabItem(
    @StringRes val title: Int,
    @StringRes val heading: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: String
)
