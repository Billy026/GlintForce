package com.billy.glintforce.data.user

/**
 * Details about the user and their set preferences.
 */
data class User(
    val userName: String = "User",
    val lang: String,
    val darkTheme: Boolean = false,
    val showWidget: Boolean = true,
    val report: Int = 0,
    val dateGenerated: String,
    val reportGenerated: Boolean = false
)

enum class UserFields {
    USERID, LANG, DARKTHEME, SHOWWIDGET, REPORT, DATEGENERATED, REPORTGENERATED
}
