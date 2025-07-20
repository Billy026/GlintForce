package com.billy.glintforce

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.SquareIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseDatabase
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseRepository
import com.billy.glintforce.data.toRemove.expenseDatabase.OfflineExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Key to enable redirection from App Widget into application
private val destinationKey = ActionParameters.Key<String>(
    NavigationActivity.KEY_DESTINATION
)

/**
 * Widget to allow redirection from Home screen of device to Add Expense screen in application
 */
class AppWidget : GlanceAppWidget() {

    // Provide data to be displayed in Widget
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sharedPreferences = context.getSharedPreferences("widget_uid", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id_key", "") ?: ""

        if (userId.isEmpty()) {
            Log.e("AppWidget", "User ID is empty")
            return
        }

        val expenseRepository = getExpenseRepository(context)
        val todayDate = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(Date())
        val currentSpending = expenseRepository.getTodaySpendingStream(todayDate, userId).first() ?: 0.0

        provideContent {
            Display(currentSpending)
        }
    }

    // Function to get Expense Repository from outside application
    private fun getExpenseRepository(context: Context): ExpenseRepository {
        return OfflineExpenseRepository(ExpenseDatabase.getDatabase(context).expenseDao())
    }

    /**
     * Composable for the UI of the Widget
     */
    @Composable
    private fun Display (currentSpending: Double) {
        Column (
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .padding(20.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Row(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Column(
                    modifier = GlanceModifier
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Today's spending:",
                        style = TextStyle(
                            fontWeight = androidx.glance.text.FontWeight.Medium,
                            color = GlanceTheme.colors.onSurface,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "S$${String.format(Locale.getDefault(), "%.2f", currentSpending)}",
                        style = TextStyle(
                            fontWeight = androidx.glance.text.FontWeight.Normal,
                            color = GlanceTheme.colors.onSurface,
                            fontSize = 14.sp
                        )
                    )
                }
                Spacer(modifier = GlanceModifier.defaultWeight())
                SquareIconButton(
                    imageProvider = ImageProvider(R.drawable.ic_add_homescreen),
                    contentDescription = "Add Expense",
                    onClick = actionStartActivity<NavigationActivity>(
                        actionParametersOf(destinationKey to "addFromWidget")
                    ),
                    modifier = GlanceModifier
                        .size(48.dp)
                )
                Spacer(modifier = GlanceModifier.width(10.dp))
                SquareIconButton(
                    imageProvider = ImageProvider(R.drawable.widget_refresh),
                    contentDescription = "Refresh",
                    onClick = {},
                    modifier = GlanceModifier
                        .size(48.dp)
                )
            }
        }
    }
}

class NavigationActivity : ComponentActivity() {
    companion object {
        const val KEY_DESTINATION = "destination"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destination = intent.extras?.getString(KEY_DESTINATION) ?: return

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(KEY_DESTINATION, destination)
        }
        startActivity(mainIntent)
        finish()
    }
}

// Receiver for refresh button on widget
class RefreshBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppWidget().updateAll(context)
            } catch (e: Exception) {
                Log.e("RefreshBroadcastReceiver", "Error updating widget", e)
            }
        }
    }
}

class AppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AppWidget()
}


