package com.example.parentalcontrol.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

private const val HOUR_IN_MILLISECONDS = 1000L * 3600L
private const val FOREGROUND_APP_CHECK_DELAY = 100L


private var previousForegroundApp: String? = null
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
suspend fun Context.observeForegroundApps() = flow<String> {
    while (currentCoroutineContext().isActive) {
        val statsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageEvents: UsageEvents? = statsManager.queryLastHourEvents()

        val event = UsageEvents.Event()
        while (currentCoroutineContext().isActive
            && usageEvents != null
            && usageEvents.hasNextEvent()
        ) {
            usageEvents.getNextEvent(event)
        }

        emit(event.packageName)

        delay(FOREGROUND_APP_CHECK_DELAY)
    }
}
    .filter { it != packageName }
    .distinctUntilChanged()

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun UsageStatsManager.queryLastHourEvents(): UsageEvents? {
    val currentTime = System.currentTimeMillis()
    return queryEvents(currentTime - HOUR_IN_MILLISECONDS, currentTime)
}
