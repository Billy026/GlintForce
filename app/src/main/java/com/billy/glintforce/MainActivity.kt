package com.billy.glintforce

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.billy.glintforce.navHosts.TemplateNavHost
import com.billy.glintforce.theme.LogInTheme
import com.billy.glintforce.viewModel.authentication.AuthState
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.main.MainViewModel
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpPermissions()

        val authViewModel: AuthViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
            val uiState by viewModel.uiState.collectAsState()

            LogInTheme {
                val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)
                val userId = (authState as? AuthState.Authenticated)?.uid
                val navController = rememberNavController()

                val isLaunchedFromWidget = isLaunchedFromWidget(
                    isFromWidget = !uiState.launch,
                    isUserIdNotNull = userId != null,
                    toggleLaunch = { viewModel.toggleLaunch() }
                )

                saveUserIdToSharedPreferences(userId = userId)

                if (authState is AuthState.Authenticated) {
                    TemplateNavHost(
                        navController = navController,
                        startDestination = "login",
                        authViewModel = authViewModel
                    )
                    /**
                    TemplateNavHost(
                        navController = navController,
                        startDestination = "main",
                        isFromWidget = isLaunchedFromWidget,
                        authViewModel = authViewModel
                    )
                    **/
                } else {
                    TemplateNavHost(
                        navController = navController,
                        startDestination = "login",
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }

    /**
     * Suppress screen orientation warning since financial tracking applications usually also
     * have their screen orientation locked.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    /**
     * Sets up permissions for the app.
     */
    private fun setUpPermissions() {
        // Locks screen orientation to portrait mode
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Requests and sets notification permissions
        requestNotificationPermission()
        scheduleNotificationWorker()
    }

    /**
     * Asks for notification permissions when the app is opened.
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1000
            )
            Log.d(
                "NotificationPermissions",
                "Notification permissions have been requested."
            )
        }
    }

    /**
     * Sets the notification's time interval to every 15 minutes.
     */
    private fun scheduleNotificationWorker() {
        val workManager = WorkManager.getInstance(this)
        val workRequestBuilder =
                PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                    .build()

        workManager.enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequestBuilder
        )
        Log.d("NotificationWorker", "Worker has been scheduled.")
    }

    /**
     * Saves the authenticated user's UID to SharedPreferences for later access.
     */
    private fun saveUserIdToSharedPreferences(userId: String?) {
        if (userId != null) {
            val sharedPreferences = getSharedPreferences("widget_uid", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("user_id_key", userId).apply()
            Log.d("SharedPreferences", "User ID saved to SharedPreferences.")
        }
    }

    /**
     * Checks whether the app opened from the widget or not.
     *
     * @param isFromWidget Whether the app opened from the widget.
     */
    private fun isLaunchedFromWidget(
        isFromWidget: Boolean,
        isUserIdNotNull: Boolean,
        toggleLaunch: () -> Unit
    ) : Boolean {
        if (isFromWidget) {
            toggleLaunch()
            return isUserIdNotNull
        }

        return false
    }

    // Trash code
    /**
    /**
     * Gets the Downloads directory in device.
     */
    private fun getDirectory(context: Context): File {
        val activity = context as Activity

        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 23
        )

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return if (dir != null && dir.exists()) dir else filesDir
    }
    **/
}

/**
 * Gets the device's Downloads directory.
 */
fun Context.getDownloadDirectory(): File {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    return if (dir != null && dir.exists()) dir else this.filesDir
}

/**
 * Gets required permissions for storage of PDF to device's Downloads folder.
 */
fun getRequiredStoragePermissions(): List<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
