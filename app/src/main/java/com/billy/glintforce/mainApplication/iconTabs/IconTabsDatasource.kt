package com.billy.glintforce.mainApplication.iconTabs

import com.billy.glintforce.R

/**
 * Class containing the list of Icon Tabs
 */
object IconTabsDatasource {
    val iconList = listOf(
        IconTabs(title =  R.string.myAccount, route = "myAccount"),
        //IconTabs(title = R.string.changeAccount),
        IconTabs(title = R.string.settings_icon, route = "settings"),
        IconTabs(title = R.string.logOut, route = "logout", isLogOut = true)
    )
}