package com.billy.glintforce

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 *  Notification is built and
 *  the worker will execute the notification while it is called by workManager
 */
class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "daily_reminder"
    }

    override fun doWork(): Result {
        Log.d("NotificationWorker", "Worker is executing.")
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val activityIntent = Intent(applicationContext, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Record your expense now")
            .setContentText("Have you recorded your expense today?")
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}