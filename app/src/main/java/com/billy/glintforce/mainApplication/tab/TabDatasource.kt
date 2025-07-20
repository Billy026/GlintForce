package com.billy.glintforce.mainApplication.tab

import com.billy.glintforce.R

/**
 * Class containing the list of Tab Items
 */
object TabDatasource {
    val tabList = listOf(
        TabItem(
            title = R.string.home_title,
            heading = R.string.hello,
            unselectedIcon = R.drawable.home_outlined,
            selectedIcon = R.drawable.home_filled,
            route = "home"
        ),
        TabItem(
            title = R.string.database_title,
            heading = R.string.database_name,
            unselectedIcon = R.drawable.database_outlined,
            selectedIcon = R.drawable.database_filled,
            route = "database"
        ),
        TabItem(
            title = R.string.limit_title,
            heading = R.string.limit_name,
            unselectedIcon = R.drawable.limit_outlined,
            selectedIcon = R.drawable.limit_filled,
            route = "limit"
        )
    )
}