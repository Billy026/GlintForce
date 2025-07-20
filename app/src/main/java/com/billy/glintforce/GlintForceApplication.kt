package com.billy.glintforce

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.billy.glintforce.mainApplication.databaseTab.DatabaseContainer
import com.billy.glintforce.mainApplication.databaseTab.DatabaseDataContainer
import com.billy.glintforce.mainApplication.limitTab.LimitContainer
import com.billy.glintforce.mainApplication.limitTab.LimitDataContainer
import com.billy.glintforce.mainApplication.settingsTab.container.SettingsContainer
import com.billy.glintforce.mainApplication.settingsTab.container.SettingsDataContainer
import com.billy.glintforce.mainApplication.settingsTab.container.UserContainer
import com.billy.glintforce.mainApplication.settingsTab.container.UserDataContainer

/**
 * Class detailing the GlintForce Application
 */
class GlintForceApplication : Application() {
    // Containers for injection of repositories
    lateinit var databaseContainer: com.billy.glintforce.mainApplication.databaseTab.DatabaseContainer
    lateinit var limitContainer: LimitContainer
    lateinit var settingsContainer: SettingsContainer
    lateinit var userContainer: UserContainer

    override fun onCreate() {
        super.onCreate()
        databaseContainer =
            DatabaseDataContainer(context = this)
        limitContainer = LimitDataContainer(context = this)
        settingsContainer = SettingsDataContainer(context = this)
        userContainer = UserDataContainer(context = this)
        createNotificationChannel()
    }

    // Sets up the notification service of the app
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationWorker.CHANNEL_ID,
                "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Daily reminder to remind users to record their expense"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}