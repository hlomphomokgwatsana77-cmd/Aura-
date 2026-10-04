package com.example.data.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.data.model.LoadsheddingArea
import com.example.data.receiver.LoadsheddingAlertReceiver
import java.util.Calendar

object LoadsheddingNotificationManager {
    const val CHANNEL_ID = "loadshedding_alerts"
    const val CHANNEL_NAME = "Load Shedding 30-Min Alerts"
    const val NOTIFICATION_ID = 2026
    private const val REQUEST_CODE = 4040

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Advance notification 30 minutes before load shedding power cuts"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 150, 300)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun schedule30MinAlert(context: Context, area: LoadsheddingArea, stage: Int) {
        createNotificationChannel(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val slots = area.scheduleByStage[stage] ?: area.scheduleByStage[2] ?: emptyList()
        val todaySlots = slots.filter { it.day.equals("Today", ignoreCase = true) }

        val cal = Calendar.getInstance()
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)
        val currentMin = cal.get(Calendar.MINUTE)
        val currentMinutes = currentHour * 60 + currentMin

        var targetSlot = todaySlots.firstOrNull { slot ->
            val startMin = parseTimeToMinutes(slot.startTime)
            startMin > currentMinutes
        }

        val targetCal = Calendar.getInstance()
        if (targetSlot != null) {
            val parts = targetSlot.startTime.split(":")
            val h = parts.getOrNull(0)?.toIntOrNull() ?: 16
            val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
            targetCal.set(Calendar.HOUR_OF_DAY, h)
            targetCal.set(Calendar.MINUTE, m)
            targetCal.set(Calendar.SECOND, 0)
        } else {
            // Check tomorrow's first slot
            val tomorrowSlot = slots.firstOrNull { it.day.equals("Tomorrow", ignoreCase = true) }
            if (tomorrowSlot != null) {
                targetSlot = tomorrowSlot
                val parts = tomorrowSlot.startTime.split(":")
                val h = parts.getOrNull(0)?.toIntOrNull() ?: 8
                val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
                targetCal.add(Calendar.DAY_OF_YEAR, 1)
                targetCal.set(Calendar.HOUR_OF_DAY, h)
                targetCal.set(Calendar.MINUTE, m)
                targetCal.set(Calendar.SECOND, 0)
            } else {
                return
            }
        }

        // Trigger 30 minutes before
        val alertTimeMillis = targetCal.timeInMillis - (30 * 60 * 1000)

        val intent = Intent(context, LoadsheddingAlertReceiver::class.java).apply {
            action = "com.example.LOADSHEDDING_30MIN_ALERT"
            putExtra("suburb", area.suburb)
            putExtra("municipality", area.municipality)
            putExtra("startTime", targetSlot.startTime)
            putExtra("endTime", targetSlot.endTime)
            putExtra("stage", stage)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertTimeMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertTimeMillis, pendingIntent)
            }
        } catch (e: Exception) {
            // Fallback gracefully
        }
    }

    fun cancelAlerts(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, LoadsheddingAlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun showOutageAlertNotification(
        context: Context,
        suburb: String,
        municipality: String,
        startTime: String,
        endTime: String,
        stage: Int
    ) {
        createNotificationChannel(context)

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚡ Load Shedding in 30 Minutes: $suburb")
            .setContentText("Power cuts start at $startTime until $endTime (Stage $stage). Boil the kettle & charge devices!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Power in $suburb ($municipality Block) is scheduled to turn OFF from $startTime to $endTime (Stage $stage).\n\nAction reminders:\n• Plug in your phone & power banks now\n• Boil water and fill your thermos\n• Unplug sensitive appliances before blackout")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setColor(0xFF9333EA.toInt())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 300, 150, 300))
            .build()

        try {
            val manager = NotificationManagerCompat.from(context)
            manager.notify(NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Handled if permission denied
        }
    }

    private fun parseTimeToMinutes(timeStr: String): Int {
        val parts = timeStr.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return h * 60 + m
    }
}
