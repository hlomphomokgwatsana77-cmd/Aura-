package com.example.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.notification.LoadsheddingNotificationManager

class LoadsheddingAlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val suburb = intent.getStringExtra("suburb") ?: "Your Area"
        val municipality = intent.getStringExtra("municipality") ?: "City Power"
        val startTime = intent.getStringExtra("startTime") ?: "soon"
        val endTime = intent.getStringExtra("endTime") ?: "later"
        val stage = intent.getIntExtra("stage", 2)

        LoadsheddingNotificationManager.showOutageAlertNotification(
            context = context,
            suburb = suburb,
            municipality = municipality,
            startTime = startTime,
            endTime = endTime,
            stage = stage
        )
    }
}
