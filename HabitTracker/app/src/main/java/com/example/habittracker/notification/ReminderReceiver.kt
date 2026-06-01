package com.example.habittracker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.habittracker.MainActivity
import com.example.habittracker.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Время для привычки"
        val id = intent.getIntExtra("id", 0)

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Напоминания о привычках",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Ежедневные напоминания выполнить привычку" }
        manager.createNotificationChannel(channel)

        val tapIntent = PendingIntent.getActivity(
            context, id,
            Intent(context, MainActivity::class.java)
                .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Время для привычки")
            .setContentText(title)
            .setAutoCancel(true)
            .setContentIntent(tapIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(id, notification)
    }

    companion object {
        const val CHANNEL_ID = "habit_reminders"
    }
}
